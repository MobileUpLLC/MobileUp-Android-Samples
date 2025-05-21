package ru.mobileup.samples.features.work_manager.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.features.R
import kotlin.math.absoluteValue

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val activityProvider: ActivityProvider,
) : Worker(context, workerParams) {

    companion object {
        const val TITLE_KEY = "title_key"
        private const val CHANNEL_ID = "reminder_channel"
    }

    override fun doWork(): Result {
        val id = System.currentTimeMillis().toInt().absoluteValue

        showNotification(
            id,
            inputData.getString(TITLE_KEY) ?: applicationContext.getString(R.string.work_notification_title),
            applicationContext.getString(R.string.work_notification_message_with_id, id),
        )

        return Result.success()
    }

    private fun showNotification(id: Int, title: String, message: String) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            applicationContext.getString(R.string.work_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        manager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, activityProvider.activity?.javaClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(id, notification)
    }
}
