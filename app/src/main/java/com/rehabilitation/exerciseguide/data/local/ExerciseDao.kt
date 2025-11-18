package com.rehabilitation.exerciseguide.data.local

import androidx.room.*
import com.rehabilitation.exerciseguide.data.local.entities.DailyProgress
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Exercise database operations
 */
@Dao
interface ExerciseDao {

    // Exercise operations
    @Query("SELECT * FROM exercises ORDER BY orderIndex ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY orderIndex ASC")
    fun getExercisesByCategory(category: ExerciseCategory): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): Exercise?

    @Query("SELECT * FROM exercises WHERE isCompleted = 1")
    fun getCompletedExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE isFavorite = 1 ORDER BY orderIndex ASC")
    fun getFavoriteExercises(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("UPDATE exercises SET isCompleted = 0")
    suspend fun resetAllExercises()

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE isCompleted = 1")
    suspend fun getCompletedCount(): Int

    // Daily Progress operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyProgress(progress: DailyProgress)

    @Query("SELECT * FROM daily_progress WHERE exerciseId = :exerciseId ORDER BY dateCompleted DESC")
    fun getProgressForExercise(exerciseId: Int): Flow<List<DailyProgress>>

    @Query("SELECT * FROM daily_progress WHERE dateCompleted >= :startDate AND dateCompleted <= :endDate")
    fun getProgressForDateRange(startDate: Long, endDate: Long): Flow<List<DailyProgress>>

    @Query("DELETE FROM daily_progress WHERE dateCompleted < :date")
    suspend fun deleteOldProgress(date: Long)

    // Statistics
    @Query("""
        SELECT COUNT(DISTINCT DATE(dateCompleted / 1000, 'unixepoch')) as streakDays
        FROM daily_progress
        WHERE dateCompleted >= :startDate
    """)
    suspend fun getStreakDays(startDate: Long): Int

    @Query("""
        SELECT AVG(completionCount) as averageCompletion
        FROM exercises
    """)
    suspend fun getAverageCompletionRate(): Float
}