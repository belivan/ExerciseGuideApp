package com.rehabilitation.exerciseguide.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rehabilitation.exerciseguide.MainActivity
import com.rehabilitation.exerciseguide.R

/**
 * BroadcastReceiver for handling daily exercise reminder notifications
 */
class DailyNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "exercise_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Create notification channel if needed
        createNotificationChannel(context)

        // Create intent to open the app when notification is tapped
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Время для упражнений! 💪")
            .setContentText("Пора делать реабилитационные упражнения для колена")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Доброе утро! Не забудьте выполнить ваши ежедневные упражнения для восстановления колена. Всего 13 упражнений для вашего здоровья!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID, notification)
            } catch (e: SecurityException) {
                // Handle case where notification permission is not granted
                e.printStackTrace()
            }
        }

        // Reschedule for tomorrow (needed for exact alarms on Android 12+)
        NotificationHelper.rescheduleNotification(context)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Напоминания об упражнениях"
            val descriptionText = "Ежедневные напоминания о реабилитационных упражнениях"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}