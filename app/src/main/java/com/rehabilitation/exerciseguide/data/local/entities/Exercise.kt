package com.rehabilitation.exerciseguide.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Exercise entity for Room database
 * Represents a rehabilitation exercise with video content
 */
@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Text content keys for localization
    val titleKey: String,           // Resource key for exercise title (e.g., "exercise_knee_bend_title")
    val descriptionKey: String,     // Resource key for description (e.g., "exercise_knee_bend_desc")
    val instructionsKey: String,    // Resource key for instructions (e.g., "exercise_knee_bend_instructions")

    // Video and media
    val videoFileName: String,      // Video file name in res/raw (e.g., "knee_bend.mp4")
    val thumbnailFileName: String?, // Optional thumbnail image

    // Exercise metadata
    val category: ExerciseCategory,
    val difficultyLevel: DifficultyLevel,
    val durationSeconds: Int,       // Video/exercise duration
    val repetitions: Int,           // Recommended repetitions
    val sets: Int,                  // Recommended sets

    // Progress tracking
    val isCompleted: Boolean = false,
    val lastCompletedDate: Long? = null,
    val completionCount: Int = 0,
    val isFavorite: Boolean = false,

    // Additional info
    val equipmentNeeded: String? = null,  // Equipment required (if any)
    val precautions: String? = null,      // Safety precautions
    val orderIndex: Int = 0               // Display order in list
)

/**
 * Exercise categories for filtering
 */
enum class ExerciseCategory {
    KNEE_REHABILITATION,    // Knee-specific exercises
    STRENGTH,              // Strength building
    FLEXIBILITY,           // Flexibility and stretching
    BALANCE,               // Balance improvement
    WARM_UP,               // Warm-up exercises
    COOL_DOWN              // Cool-down exercises
}

/**
 * Difficulty levels for exercises
 */
enum class DifficultyLevel {
    EASY,      // Beginner-friendly
    MEDIUM,    // Intermediate
    HARD       // Advanced
}

/**
 * Data class for tracking daily progress
 */
@Entity(tableName = "daily_progress")
data class DailyProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val dateCompleted: Long,        // Timestamp
    val setsCompleted: Int,
    val repetitionsCompleted: Int,
    val notes: String? = null        // Optional user notes
)