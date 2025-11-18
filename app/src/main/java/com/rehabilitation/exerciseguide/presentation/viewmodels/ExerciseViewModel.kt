package com.rehabilitation.exerciseguide.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rehabilitation.exerciseguide.data.local.ExerciseDatabase
import com.rehabilitation.exerciseguide.data.local.entities.DifficultyLevel
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory
import com.rehabilitation.exerciseguide.data.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing exercise data and UI state
 */
class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExerciseRepository

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val exerciseDao = ExerciseDatabase.getDatabase(application).exerciseDao()
        repository = ExerciseRepository(exerciseDao)

        loadExercises()
        initializeSampleData()
    }

    /**
     * Load all exercises from the database
     */
    private fun loadExercises() {
        viewModelScope.launch {
            repository.getAllExercises().collect { exerciseList ->
                _exercises.value = exerciseList
            }
        }
    }

    /**
     * Toggle exercise completion status
     */
    fun toggleExerciseCompletion(exercise: Exercise) {
        viewModelScope.launch {
            val updatedExercise = exercise.copy(
                isCompleted = !exercise.isCompleted,
                lastCompletedDate = if (!exercise.isCompleted) System.currentTimeMillis() else exercise.lastCompletedDate,
                completionCount = if (!exercise.isCompleted) exercise.completionCount + 1 else exercise.completionCount
            )
            repository.updateExercise(updatedExercise)
        }
    }

    /**
     * Mark exercise as completed
     */
    fun markExerciseCompleted(exercise: Exercise) {
        viewModelScope.launch {
            if (!exercise.isCompleted) {
                val updatedExercise = exercise.copy(
                    isCompleted = true,
                    lastCompletedDate = System.currentTimeMillis(),
                    completionCount = exercise.completionCount + 1
                )
                repository.updateExercise(updatedExercise)
            }
        }
    }

    /**
     * Delete an exercise
     */
    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.deleteExercise(exercise)
        }
    }

    /**
     * Add a new exercise
     */
    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.insertExercise(exercise)
        }
    }

    /**
     * Update an existing exercise
     */
    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.updateExercise(exercise)
        }
    }

    /**
     * Reset daily progress (call this at midnight or app launch)
     */
    fun resetDailyProgress() {
        viewModelScope.launch {
            repository.resetDailyProgress()
        }
    }

    /**
     * Reset all exercises for a new day
     */
    fun startNewDay() {
        viewModelScope.launch {
            _exercises.value.forEach { exercise ->
                if (exercise.isCompleted) {
                    val updatedExercise = exercise.copy(isCompleted = false)
                    repository.updateExercise(updatedExercise)
                }
            }
        }
    }

    /**
     * Initialize database with sample exercises if empty
     */
    private fun initializeSampleData() {
        viewModelScope.launch {
            val count = repository.getExerciseCount()
            if (count == 0) {
                val sampleExercises = createSampleExercises()
                sampleExercises.forEach { exercise ->
                    repository.insertExercise(exercise)
                }
            }
        }
    }

    /**
     * Create sample exercises for the app
     */
    private fun createSampleExercises(): List<Exercise> {
        // Generate exercises for each video file (1-13)
        val exercises = mutableListOf<Exercise>()

        for (i in 1..13) {
            exercises.add(
                Exercise(
                    titleKey = "Упражнение $i",
                    descriptionKey = "Реабилитационное упражнение",
                    instructionsKey = "Выполняйте медленно и плавно, следуя видео инструкции",
                    videoFileName = "exercise_$i",
                    thumbnailFileName = "thumb_exercise_$i",
                    category = ExerciseCategory.KNEE_REHABILITATION,
                    difficultyLevel = DifficultyLevel.EASY,
                    durationSeconds = 30,
                    repetitions = 10,
                    sets = 3,
                    orderIndex = i
                )
            )
        }

        return exercises
    }
}