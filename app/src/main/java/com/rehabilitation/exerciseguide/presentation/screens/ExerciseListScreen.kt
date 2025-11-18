package com.rehabilitation.exerciseguide.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.rehabilitation.exerciseguide.R
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory
import com.rehabilitation.exerciseguide.data.local.entities.DifficultyLevel
import android.net.Uri
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Main exercise list screen with horizontal carousel navigation
 * Features large touch targets and arrow navigation
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExerciseListScreen(
    exercises: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit,
    onMarkComplete: (Exercise) -> Unit,
    onDeleteExercise: (Exercise) -> Unit,
    onUpdateExercise: (Exercise) -> Unit,
    onAddExercise: () -> Unit,
    onStartNewDay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { exercises.size })
    val coroutineScope = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Progress Summary with prominent statistics - minimized for more video space
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            val completedCount = exercises.count { it.isCompleted }
            val percentage = (completedCount * 100 / exercises.size.coerceAtLeast(1))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        when {
                            percentage >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            percentage >= 50 -> Color(0xFFFFC107).copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        }
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Main progress statistics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Completed count box
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (completedCount > 0)
                                Color(0xFF4CAF50).copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (completedCount > 0) Color(0xFF4CAF50) else Color.Gray
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ВЫПОЛНЕНО",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$completedCount из ${exercises.size}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (completedCount > 0) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Percentage box
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                percentage >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                percentage >= 50 -> Color(0xFFFFC107).copy(alpha = 0.2f)
                                percentage > 0 -> Color(0xFF2196F3).copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                        border = BorderStroke(
                            2.dp,
                            when {
                                percentage >= 80 -> Color(0xFF4CAF50)
                                percentage >= 50 -> Color(0xFFFFC107)
                                percentage > 0 -> Color(0xFF2196F3)
                                else -> Color.Gray
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ПРОГРЕСС",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$percentage%",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = when {
                                    percentage >= 80 -> Color(0xFF4CAF50)
                                    percentage >= 50 -> Color(0xFFFFC107)
                                    percentage > 0 -> Color(0xFF2196F3)
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Start/New Day button in statistics section - smaller for more video space
                val completedCount = exercises.count { it.isCompleted }
                Button(
                    onClick = onStartNewDay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (completedCount > 0)
                            Color(0xFFFF6B6B)
                        else
                            Color(0xFF4CAF50)
                    ),
                    border = BorderStroke(
                        2.dp,
                        if (completedCount > 0) Color(0xFFFF6B6B) else Color(0xFF4CAF50)
                    )
                ) {
                    Icon(
                        imageVector = if (completedCount > 0) Icons.Default.Refresh else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (completedCount > 0) "НАЧАТЬ НОВЫЙ ДЕНЬ" else "НАЧАТЬ ТРЕНИРОВКУ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Edit Mode Controls and Exercise Counter - smaller for more video space
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .height(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Current Exercise Indicator
            if (exercises.isNotEmpty()) {
                Text(
                    text = "Упражнение ${pagerState.currentPage + 1} из ${exercises.size}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.alignByBaseline()
                )
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            // Edit/Done button - subtle design
            if (isEditMode) {
                TextButton(
                    onClick = { isEditMode = false },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.alignByBaseline()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Готово", fontSize = 12.sp, color = Color(0xFF4CAF50))
                }
            } else {
                TextButton(
                    onClick = { isEditMode = true },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.alignByBaseline()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Изменить", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Horizontal Pager for Exercises
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (exercises.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val exercise = exercises[page]
                    val isVisible = page == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LargeExerciseCard(
                            exercise = exercise,
                            exerciseNumber = page + 1,
                            onExerciseClick = { onExerciseClick(exercise) },
                            onMarkComplete = { onMarkComplete(exercise) },
                            isEditMode = isEditMode,
                            onDelete = { if (isEditMode) onDeleteExercise(exercise) },
                            onUpdate = onUpdateExercise,
                            isVisible = isVisible
                        )
                    }
                }

                // Navigation Arrows
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Previous Exercise Button
                    if (pagerState.currentPage > 0) {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            modifier = Modifier.size(80.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = "Previous",
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "№${pagerState.currentPage}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(80.dp))
                    }

                    // Next Exercise Button
                    if (pagerState.currentPage < exercises.size - 1) {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            modifier = Modifier.size(80.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Next",
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "№${pagerState.currentPage + 2}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.size(80.dp))
                    }
                }
            }
        }
    }
}

/**
 * Large exercise card for carousel view with embedded video player
 * Shows one exercise at a time with prominent display
 */
@Composable
fun LargeExerciseCard(
    exercise: Exercise,
    exerciseNumber: Int,
    onExerciseClick: () -> Unit,
    onMarkComplete: () -> Unit,
    isEditMode: Boolean = false,
    onDelete: () -> Unit = {},
    onUpdate: (Exercise) -> Unit = {},
    isVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isShowingVideo by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var isCompleted by remember { mutableStateOf(false) }

    // Edit dialog state
    var showEditDialog by remember { mutableStateOf(false) }
    var editedName by remember(exercise.id) { mutableStateOf(exercise.titleKey) }
    var editedReps by remember(exercise.id) { mutableStateOf(exercise.repetitions.toString()) }
    var editedSets by remember(exercise.id) { mutableStateOf(exercise.sets.toString()) }
    var editedDuration by remember(exercise.id) { mutableStateOf(exercise.durationSeconds.toString()) }

    // Reset edit dialog when exercise changes
    LaunchedEffect(exercise.id) {
        editedName = exercise.titleKey
        editedReps = exercise.repetitions.toString()
        editedSets = exercise.sets.toString()
        editedDuration = exercise.durationSeconds.toString()
        showEditDialog = false
    }

    // Create ExoPlayer when video is shown
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }

    // Stop and cleanup video when page is not visible or when video should be hidden
    LaunchedEffect(isVisible, isShowingVideo) {
        if (!isVisible) {
            // Page is not visible, cleanup everything
            exoPlayer?.let { player ->
                player.pause()
                player.stop()
                player.clearMediaItems()
                player.release()
            }
            exoPlayer = null
            isShowingVideo = false
            isCompleted = false
        } else if (isShowingVideo && exoPlayer == null) {
            // Page is visible and we need to show video
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                val videoUri = Uri.parse("android.resource://${context.packageName}/raw/${exercise.videoFileName.removeSuffix(".mp4")}")
                setMediaItem(MediaItem.fromUri(videoUri))
                prepare()
                playWhenReady = true // Auto-play when ready
                repeatMode = Player.REPEAT_MODE_OFF
            }
        } else if (!isShowingVideo && exoPlayer != null) {
            // Video should be hidden, cleanup
            exoPlayer?.let { player ->
                player.pause()
                player.stop()
                player.clearMediaItems()
                player.release()
            }
            exoPlayer = null
        }
    }

    // Lifecycle management and final cleanup
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val currentPlayer = exoPlayer
        val observer = if (currentPlayer != null) {
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        currentPlayer.pause()
                        isPlaying = false
                    }
                    else -> {}
                }
            }.also { lifecycleOwner.lifecycle.addObserver(it) }
        } else null

        onDispose {
            observer?.let { lifecycleOwner.lifecycle.removeObserver(it) }
            currentPlayer?.let { player ->
                player.stop()
                player.clearMediaItems()
                player.release()
            }
        }
    }

    // Update playback state
    LaunchedEffect(exoPlayer) {
        val currentPlayer = exoPlayer
        if (currentPlayer != null) {
            while (isActive) {
                isPlaying = currentPlayer.isPlaying
                currentPosition = currentPlayer.currentPosition
                duration = currentPlayer.duration.coerceAtLeast(1L)

                // Check if video completed
                if (duration > 0 && currentPosition > duration - 500 && !isCompleted) {
                    isCompleted = true
                    // Auto-mark as complete when video finishes
                    onMarkComplete()
                }

                delay(100)
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (exercise.isCompleted) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Large Video/Player Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Take up most of the space
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (isShowingVideo && exoPlayer != null) {
                    // Show video player
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                player = exoPlayer
                                useController = false
                                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Play/Pause button at top left
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        IconButton(
                            onClick = {
                                if (isPlaying) {
                                    exoPlayer?.pause()
                                } else {
                                    exoPlayer?.play()
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (!isPlaying)
                                        Color(0xFF4CAF50).copy(alpha = 0.8f)
                                    else
                                        Color.Black.copy(alpha = 0.7f)
                                )
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }

                    // Close button at top right
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = {
                                exoPlayer?.let { player ->
                                    player.pause()
                                    player.stop()
                                    player.clearMediaItems()
                                    player.release()
                                }
                                exoPlayer = null
                                isShowingVideo = false
                                isPlaying = false
                                currentPosition = 0L
                                duration = 0L
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Red.copy(alpha = 0.8f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(24.dp),
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    // Show thumbnail with play button
                    val thumbnailResId = when (exercise.videoFileName) {
                        "exercise_1" -> R.drawable.thumb_exercise_1
                        "exercise_2" -> R.drawable.thumb_exercise_2
                        "exercise_3" -> R.drawable.thumb_exercise_3
                        "exercise_4" -> R.drawable.thumb_exercise_4
                        "exercise_5" -> R.drawable.thumb_exercise_5
                        "exercise_6" -> R.drawable.thumb_exercise_6
                        "exercise_7" -> R.drawable.thumb_exercise_7
                        "exercise_8" -> R.drawable.thumb_exercise_8
                        "exercise_9" -> R.drawable.thumb_exercise_9
                        "exercise_10" -> R.drawable.thumb_exercise_10
                        "exercise_11" -> R.drawable.thumb_exercise_11
                        "exercise_12" -> R.drawable.thumb_exercise_12
                        "exercise_13" -> R.drawable.thumb_exercise_13
                        else -> 0
                    }

                    if (thumbnailResId != 0) {
                        Image(
                            painter = painterResource(id = thumbnailResId),
                            contentDescription = exercise.titleKey,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Play button overlay
                    FloatingActionButton(
                        onClick = { isShowingVideo = true },
                        modifier = Modifier.size(80.dp),
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            // Compact Title and Metrics Row
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Exercise Title
                Text(
                    text = exercise.titleKey,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    maxLines = 1
                )

                // Compact Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Repetitions
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${exercise.repetitions} повтор.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Sets
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Numbers,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${exercise.sets} подх.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Duration
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${exercise.durationSeconds}с",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Action Buttons
            if (isEditMode) {
                // Edit and Delete buttons in edit mode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Edit button
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Изменить",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Delete button
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFE53935)
                        ),
                        border = BorderStroke(2.dp, Color(0xFFE53935))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Удалить",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Complete button
                Button(
                    onClick = onMarkComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (exercise.isCompleted)
                            Color(0xFF4CAF50)
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (exercise.isCompleted)
                            Icons.Default.CheckCircle
                        else
                            Icons.Outlined.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (exercise.isCompleted) "Выполнено" else "Отметить выполненным",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Edit Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Изменить упражнение",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Exercise Name
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Название упражнения") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Repetitions
                    OutlinedTextField(
                        value = editedReps,
                        onValueChange = { value ->
                            // Only allow numbers
                            if (value.all { it.isDigit() } || value.isEmpty()) {
                                editedReps = value
                            }
                        },
                        label = { Text("Повторения") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Sets
                    OutlinedTextField(
                        value = editedSets,
                        onValueChange = { value ->
                            // Only allow numbers
                            if (value.all { it.isDigit() } || value.isEmpty()) {
                                editedSets = value
                            }
                        },
                        label = { Text("Подходы") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Numbers,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )

                    // Duration
                    OutlinedTextField(
                        value = editedDuration,
                        onValueChange = { value ->
                            // Only allow numbers
                            if (value.all { it.isDigit() } || value.isEmpty()) {
                                editedDuration = value
                            }
                        },
                        label = { Text("Длительность (секунды)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Update the exercise with new values
                        val updatedExercise = exercise.copy(
                            titleKey = editedName.ifBlank { exercise.titleKey },
                            repetitions = editedReps.toIntOrNull() ?: exercise.repetitions,
                            sets = editedSets.toIntOrNull() ?: exercise.sets,
                            durationSeconds = editedDuration.toIntOrNull() ?: exercise.durationSeconds
                        )
                        onUpdate(updatedExercise)
                        showEditDialog = false
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Reset values and close
                        editedName = exercise.titleKey
                        editedReps = exercise.repetitions.toString()
                        editedSets = exercise.sets.toString()
                        editedDuration = exercise.durationSeconds.toString()
                        showEditDialog = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

/**
 * Individual exercise card with large touch targets
 * Minimum height of 120dp for easy tapping
 */
@Composable
fun ExerciseCard(
    exercise: Exercise,
    exerciseNumber: Int,
    isEditMode: Boolean,
    onExerciseClick: () -> Unit,
    onMarkComplete: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp) // Large touch target
            .clickable(onClick = onExerciseClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (exercise.isCompleted) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Video Thumbnail
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // Get thumbnail resource ID directly
                val thumbnailResId = when (exercise.videoFileName) {
                    "exercise_1" -> R.drawable.thumb_exercise_1
                    "exercise_2" -> R.drawable.thumb_exercise_2
                    "exercise_3" -> R.drawable.thumb_exercise_3
                    "exercise_4" -> R.drawable.thumb_exercise_4
                    "exercise_5" -> R.drawable.thumb_exercise_5
                    "exercise_6" -> R.drawable.thumb_exercise_6
                    "exercise_7" -> R.drawable.thumb_exercise_7
                    "exercise_8" -> R.drawable.thumb_exercise_8
                    "exercise_9" -> R.drawable.thumb_exercise_9
                    "exercise_10" -> R.drawable.thumb_exercise_10
                    "exercise_11" -> R.drawable.thumb_exercise_11
                    "exercise_12" -> R.drawable.thumb_exercise_12
                    "exercise_13" -> R.drawable.thumb_exercise_13
                    else -> 0
                }

                if (thumbnailResId != 0) {
                    Image(
                        painter = painterResource(id = thumbnailResId),
                        contentDescription = exercise.titleKey,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Default video icon
                    Icon(
                        imageVector = Icons.Default.PlayCircleFilled,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Exercise Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Exercise Title - display any title properly
                Text(
                    text = exercise.titleKey,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 24.sp,
                    softWrap = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Duration badge
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.heightIn(min = 28.dp)
                    ) {
                        Text(
                            text = "${exercise.durationSeconds}с",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            maxLines = 1
                        )
                    }

                    // Difficulty badge
                    Badge(
                        containerColor = when (exercise.difficultyLevel) {
                            DifficultyLevel.EASY -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            DifficultyLevel.MEDIUM -> Color(0xFFFFC107).copy(alpha = 0.2f)
                            DifficultyLevel.HARD -> Color(0xFFF44336).copy(alpha = 0.2f)
                        },
                        modifier = Modifier.heightIn(min = 28.dp)
                    ) {
                        Text(
                            text = when (exercise.difficultyLevel) {
                                DifficultyLevel.EASY -> stringResource(R.string.difficulty_easy)
                                DifficultyLevel.MEDIUM -> stringResource(R.string.difficulty_medium)
                                DifficultyLevel.HARD -> stringResource(R.string.difficulty_hard)
                            },
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = when (exercise.difficultyLevel) {
                                DifficultyLevel.EASY -> Color(0xFF2E7D32)
                                DifficultyLevel.MEDIUM -> Color(0xFFF57C00)
                                DifficultyLevel.HARD -> Color(0xFFC62828)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sets and repetitions
                Text(
                    text = "${exercise.sets} × ${exercise.repetitions}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Action button: Delete in edit mode, Checkbox in normal mode
            if (isEditMode) {
                // Delete button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(64.dp) // Large touch target
                        .clip(CircleShape)
                        .background(Color(0xFFF44336).copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_exercise),
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFFF44336)
                    )
                }
            } else {
                // Large Completion Checkbox
                IconButton(
                    onClick = onMarkComplete,
                    modifier = Modifier
                        .size(64.dp) // Large touch target
                        .clip(CircleShape)
                        .background(
                            if (exercise.isCompleted) {
                                Color(0xFF4CAF50).copy(alpha = 0.1f)
                            } else {
                                Color.Transparent
                            }
                        )
                ) {
                    Icon(
                        imageVector = if (exercise.isCompleted) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.RadioButtonUnchecked
                        },
                        contentDescription = stringResource(
                            if (exercise.isCompleted) R.string.mark_incomplete
                            else R.string.mark_complete
                        ),
                        modifier = Modifier.size(48.dp),
                        tint = if (exercise.isCompleted) {
                            Color(0xFF4CAF50)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Category filter row (optional - can be removed for simpler UI)
 */
@Composable
fun CategoryFilterRow(
    selectedCategory: ExerciseCategory?,
    onCategorySelected: (ExerciseCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // "All" chip
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = {
                    Text(
                        text = stringResource(R.string.all_exercises),
                        fontSize = 18.sp
                    )
                },
                modifier = Modifier.height(48.dp)
            )
        }

        // Category chips
        items(ExerciseCategory.values().toList()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = when (category) {
                            ExerciseCategory.KNEE_REHABILITATION -> stringResource(R.string.category_knee)
                            ExerciseCategory.STRENGTH -> stringResource(R.string.category_strength)
                            ExerciseCategory.FLEXIBILITY -> stringResource(R.string.category_flexibility)
                            ExerciseCategory.BALANCE -> stringResource(R.string.category_balance)
                            ExerciseCategory.WARM_UP -> stringResource(R.string.category_warmup)
                            ExerciseCategory.COOL_DOWN -> stringResource(R.string.category_cooldown)
                        },
                        fontSize = 18.sp
                    )
                },
                modifier = Modifier.height(48.dp)
            )
        }
    }
}