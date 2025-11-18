package com.rehabilitation.exerciseguide

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rehabilitation.exerciseguide.notifications.NotificationHelper
import com.rehabilitation.exerciseguide.presentation.screens.AddExerciseScreen
import com.rehabilitation.exerciseguide.presentation.screens.ExerciseListScreen
import com.rehabilitation.exerciseguide.presentation.theme.ExerciseGuideTheme
import com.rehabilitation.exerciseguide.presentation.viewmodels.ExerciseViewModel

class MainActivity : ComponentActivity() {

    // Permission request launcher for notifications
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, schedule notifications
            NotificationHelper.scheduleDailyNotification(this)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // Override system font scaling to maintain consistent UI
        val config = Configuration(newBase.resources.configuration)
        config.fontScale = 1.0f // Force normal font scale, ignore system accessibility settings
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the action bar/title bar
        actionBar?.hide()

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Request notification permission and schedule daily reminder
        setupDailyNotifications()

        setContent {
            ExerciseGuideTheme {
                ExerciseGuideApp()
            }
        }
    }

    private fun setupDailyNotifications() {
        // Check if we need to request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    NotificationHelper.scheduleDailyNotification(this)
                }
                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No need for permission on older Android versions
            NotificationHelper.scheduleDailyNotification(this)
        }
    }
}

/**
 * Main app composable with navigation
 */
@Composable
fun ExerciseGuideApp() {
    val navController = rememberNavController()
    val exerciseViewModel: ExerciseViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            viewModel = exerciseViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Navigation setup for the app
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
    modifier: Modifier = Modifier
) {
    val exercises by viewModel.exercises.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "exercise_list",
        modifier = modifier
    ) {
        // Exercise List Screen
        composable("exercise_list") {
            ExerciseListScreen(
                exercises = exercises,
                onExerciseClick = { exercise ->
                    // Video plays in-place, no navigation needed
                },
                onMarkComplete = { exercise ->
                    viewModel.toggleExerciseCompletion(exercise)
                },
                onDeleteExercise = { exercise ->
                    viewModel.deleteExercise(exercise)
                },
                onUpdateExercise = { exercise ->
                    viewModel.updateExercise(exercise)
                },
                onAddExercise = {
                    navController.navigate("add_exercise")
                },
                onStartNewDay = {
                    viewModel.startNewDay()
                }
            )
        }

        // Add Exercise Screen
        composable("add_exercise") {
            AddExerciseScreen(
                onSaveExercise = { exercise ->
                    viewModel.addExercise(exercise)
                    navController.popBackStack()
                },
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}