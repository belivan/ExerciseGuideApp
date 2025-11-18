package com.rehabilitation.exerciseguide.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rehabilitation.exerciseguide.R
import com.rehabilitation.exerciseguide.data.local.entities.DifficultyLevel
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory

/**
 * Screen for adding new exercises
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    onSaveExercise: (Exercise) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseName by remember { mutableStateOf("") }
    var exerciseDescription by remember { mutableStateOf("") }
    var exerciseInstructions by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("30") }
    var repetitions by remember { mutableStateOf("10") }
    var sets by remember { mutableStateOf("3") }
    var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.EASY) }
    var selectedCategory by remember { mutableStateOf(ExerciseCategory.KNEE_REHABILITATION) }
    var selectedVideo by remember { mutableStateOf<String?>(null) }
    var showVideoDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.add_exercise),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // Form Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exercise Name
            OutlinedTextField(
                value = exerciseName,
                onValueChange = { exerciseName = it },
                label = { Text(stringResource(R.string.exercise_name), fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                singleLine = true
            )

            // Description (Optional)
            OutlinedTextField(
                value = exerciseDescription,
                onValueChange = { exerciseDescription = it },
                label = { Text(stringResource(R.string.exercise_description_optional), fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                minLines = 2,
                maxLines = 3
            )

            // Instructions
            OutlinedTextField(
                value = exerciseInstructions,
                onValueChange = { exerciseInstructions = it },
                label = { Text(stringResource(R.string.exercise_instructions), fontSize = 18.sp) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                minLines = 3,
                maxLines = 5
            )

            // Duration, Repetitions, Sets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource(R.string.duration_seconds), fontSize = 16.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = repetitions,
                    onValueChange = { repetitions = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource(R.string.reps), fontSize = 16.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                OutlinedTextField(
                    value = sets,
                    onValueChange = { sets = it.filter { char -> char.isDigit() } },
                    label = { Text(stringResource(R.string.sets_short), fontSize = 16.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Difficulty Selection
            Column {
                Text(
                    text = stringResource(R.string.difficulty),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DifficultyLevel.values().forEach { level ->
                        FilterChip(
                            selected = selectedDifficulty == level,
                            onClick = { selectedDifficulty = level },
                            label = {
                                Text(
                                    text = when (level) {
                                        DifficultyLevel.EASY -> stringResource(R.string.difficulty_easy)
                                        DifficultyLevel.MEDIUM -> stringResource(R.string.difficulty_medium)
                                        DifficultyLevel.HARD -> stringResource(R.string.difficulty_hard)
                                    },
                                    fontSize = 16.sp
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Video Selection Card
            Card(
                onClick = { showVideoDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedVideo != null)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (selectedVideo != null) Icons.Default.CheckCircle else Icons.Default.VideoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = if (selectedVideo != null)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedVideo != null) {
                            when (selectedVideo) {
                                "knee_bend" -> "Сгибания колена"
                                "leg_raise" -> "Подъемы прямой ноги"
                                "heel_slide" -> "Скольжение пятки"
                                "quad_sets" -> "Напряжение четырехглавой мышцы"
                                "ankle_pumps" -> "Движения голеностопом"
                                else -> selectedVideo ?: ""
                            }
                        } else {
                            stringResource(R.string.video_optional)
                        },
                        fontSize = 16.sp,
                        fontWeight = if (selectedVideo != null) FontWeight.Medium else FontWeight.Normal,
                        color = if (selectedVideo != null)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = {
                    // Create new exercise
                    val newExercise = Exercise(
                        id = 0, // Will be auto-generated by database
                        titleKey = exerciseName.ifEmpty { "New Exercise" },
                        descriptionKey = exerciseDescription.ifEmpty { "" },
                        instructionsKey = exerciseInstructions.ifEmpty { "Follow the instructions" },
                        videoFileName = selectedVideo ?: "placeholder",
                        thumbnailFileName = null,
                        category = selectedCategory,
                        difficultyLevel = selectedDifficulty,
                        durationSeconds = duration.toIntOrNull() ?: 30,
                        repetitions = repetitions.toIntOrNull() ?: 10,
                        sets = sets.toIntOrNull() ?: 3,
                        orderIndex = 999 // Will be at the end
                    )
                    onSaveExercise(newExercise)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = exerciseName.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.save_exercise),
                    fontSize = 18.sp
                )
            }
        }
    }

    // Video Selection Dialog
    if (showVideoDialog) {
        AlertDialog(
            onDismissRequest = { showVideoDialog = false },
            title = {
                Text(
                    text = "Выберите видео упражнения",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // List of available videos
                    val availableVideos = listOf(
                        "knee_bend" to "Сгибания колена",
                        "leg_raise" to "Подъемы прямой ноги",
                        "heel_slide" to "Скольжение пятки",
                        "quad_sets" to "Напряжение четырехглавой мышцы",
                        "ankle_pumps" to "Движения голеностопом"
                    )

                    availableVideos.forEach { (videoId, videoName) ->
                        Card(
                            onClick = {
                                selectedVideo = videoId
                                showVideoDialog = false
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedVideo == videoId)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                            ),
                            border = if (selectedVideo == videoId)
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            else
                                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = videoName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Option to not select video
                    TextButton(
                        onClick = {
                            selectedVideo = null
                            showVideoDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Без видео",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showVideoDialog = false }
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontSize = 16.sp
                    )
                }
            }
        )
    }
}