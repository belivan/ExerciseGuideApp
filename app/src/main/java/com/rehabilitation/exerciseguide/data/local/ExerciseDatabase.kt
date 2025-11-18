package com.rehabilitation.exerciseguide.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.rehabilitation.exerciseguide.data.local.entities.DailyProgress
import com.rehabilitation.exerciseguide.data.local.entities.DifficultyLevel
import com.rehabilitation.exerciseguide.data.local.entities.Exercise
import com.rehabilitation.exerciseguide.data.local.entities.ExerciseCategory

/**
 * Room database for the Exercise Guide app
 */
@Database(
    entities = [Exercise::class, DailyProgress::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExerciseDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                )
                    .fallbackToDestructiveMigration() // For development only
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * Type converters for Room database
 */
class Converters {

    @TypeConverter
    fun fromExerciseCategory(category: ExerciseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toExerciseCategory(category: String): ExerciseCategory {
        return ExerciseCategory.valueOf(category)
    }

    @TypeConverter
    fun fromDifficultyLevel(level: DifficultyLevel): String {
        return level.name
    }

    @TypeConverter
    fun toDifficultyLevel(level: String): DifficultyLevel {
        return DifficultyLevel.valueOf(level)
    }
}