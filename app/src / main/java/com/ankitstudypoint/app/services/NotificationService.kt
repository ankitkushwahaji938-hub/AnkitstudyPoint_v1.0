package com.ankitstudypoint.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.ankitstudypoint.app.AppConstants
import com.ankitstudypoint.app.MainActivity
import com.ankitstudypoint.app.R
import java.util.concurrent.TimeUnit

class NotificationService(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotificationChannel()
        sendStudyReminderNotification()
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AppConstants.NOTIFICATION_CHANNEL_ID,
                AppConstants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Study reminders from Ankit Study Point"
            }
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun sendStudyReminderNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val messages = listOf(
            "📚 Time to study! Open Ankit Study Point",
            "🎯 New quiz available — test your knowledge!",
            "⏱️ Start a study session today!",
            "💊 Revise your Pharma notes now!",
            "🔬 New Pharmacognosy content waiting for you!"
        )
        val message = messages.random()

        val notification = NotificationCompat.Builder(context, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Ankit Study Point 📖")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 1001

        fun scheduleDailyReminder(context: Context) {
            val request = PeriodicWorkRequestBuilder<NotificationService>(24, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS)
                .addTag(AppConstants.WORK_TAG_SYNC)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                AppConstants.WORK_TAG_SYNC,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancelReminder(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(AppConstants.WORK_TAG_SYNC)
        }
    }
}
