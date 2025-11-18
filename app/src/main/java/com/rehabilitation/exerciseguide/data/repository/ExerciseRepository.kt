package com.rehabilitation.exerciseguide.data.repository

import com.rehabilitation.exerciseguide.data.local.ExerciseDao
import com.rehabilitation.exerciseguide.data.local.entities.DailyProgress
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Repository class for managing exercise data
 * Provides a clean API for the ViewModel to access data
 */
class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    // Exercise operations
    fun getAllExercises(): Flow<List<Exercise>> = exerciseDao.getAllExercises()

    fun getExercisesByCategory(category: ExerciseCategory): Flow<List<Exercise>> =
        exerciseDao.getExercisesByCategory(category)

    suspend fun getExerciseById(id: Int): Exercise? = exerciseDao.getExerciseById(id)

    fun getCompletedExercises(): Flow<List<Exercise>> = exerciseDao.getCompletedExercises()

    fun getFavoriteExercises(): Flow<List<Exercise>> = exerciseDao.getFavoriteExercises()

    suspend fun insertExercise(exercise: Exercise) = exerciseDao.insertExercise(exercise)

    suspend fun insertExercises(exercises: List<Exercise>) = exerciseDao.insertExercises(exercises)

    suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise)

        // Also log to daily progress if completed
        if (exercise.isCompleted) {
            val progress = DailyProgress(
                exerciseId = exercise.id,
                dateCompleted = System.currentTimeMillis(),
                setsCompleted = exercise.sets,
                repetitionsCompleted = exercise.repetitions
            )
            exerciseDao.insertDailyProgress(progress)
        }
    }

    suspend fun deleteExercise(exercise: Exercise) = exerciseDao.deleteExercise(exercise)

    suspend fun getExerciseCount(): Int = exerciseDao.getExerciseCount()

    suspend fun getCompletedCount(): Int = exerciseDao.getCompletedCount()

    // Reset daily progress (typically called at midnight or app launch)
    suspend fun resetDailyProgress() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = calendar.timeInMillis

        // Check if we need to reset (if last completed date is before today)
        val exercises = exerciseDao.getAllExercises()
        exercises.collect { exerciseList ->
            exerciseList.forEach { exercise ->
                if (exercise.isCompleted && exercise.lastCompletedDate != null) {
                    if (exercise.lastCompletedDate < todayStart) {
                        // Reset completion status for exercises completed before today
                        val updatedExercise = exercise.copy(isCompleted = false)
                        exerciseDao.updateExercise(updatedExercise)
                    }
                }
            }
        }
    }

    // Daily Progress operations
    fun getProgressForExercise(exerciseId: Int): Flow<List<DailyProgress>> =
        exerciseDao.getProgressForExercise(exerciseId)

    fun getProgressForDateRange(startDate: Long, endDate: Long): Flow<List<DailyProgress>> =
        exerciseDao.getProgressForDateRange(startDate, endDate)

    suspend fun deleteOldProgress(daysToKeep: Int = 30) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysToKeep)
        exerciseDao.deleteOldProgress(calendar.timeInMillis)
    }

    // Statistics
    suspend fun getStreakDays(): Int {
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
        return exerciseDao.getStreakDays(thirtyDaysAgo)
    }

    suspend fun getAverageCompletionRate(): Float = exerciseDao.getAverageCompletionRate()

    suspend fun getCompletionPercentage(): Int {
        val total = getExerciseCount()
        if (total == 0) return 0
        val completed = getCompletedCount()
        return (completed * 100) / total
    }
}