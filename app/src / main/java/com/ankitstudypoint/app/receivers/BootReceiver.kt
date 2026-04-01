package com.ankitstudypoint.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ankitstudypoint.app.services.NotificationService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationService.scheduleDailyReminder(context)
        }
    }
}
