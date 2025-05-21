package ru.mobileup.samples.features.work_manager.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.work_manager.data.workers.NotificationWorker
import java.time.Duration

class ReminderSchedulerImpl(
    private val workManager: WorkManager,
    private val context: Context,
) : ReminderScheduler {

    companion object {
        private const val PERIODIC_WORK_NAME = "periodic_notification"
        private const val ONE_TIME_WORK_NAME = "one_time_notification"
    }

    override val periodicReminderState =
        workManager.getWorkInfosForUniqueWorkFlow(PERIODIC_WORK_NAME)

    override fun schedulePeriodicReminder(interval: Duration, initialDelay: Duration) {
        val request = PeriodicWorkRequestBuilder<NotificationWorker>(interval)
            .setInitialDelay(initialDelay)
            .setInputData(
                Data.Builder()
                    .putString(
                        NotificationWorker.TITLE_KEY,
                        context.getString(R.string.work_periodic_notification_title)
                    )
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request
        )
    }

    override fun cancelPeriodicReminder() {
        workManager.cancelUniqueWork(PERIODIC_WORK_NAME)
    }

    override val oneTimeReminderState =
        workManager.getWorkInfosForUniqueWorkFlow(ONE_TIME_WORK_NAME)

    override fun scheduleOneTimeReminder(initialDelay: Duration) {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay)
            .setInputData(
                Data.Builder()
                    .putString(
                        NotificationWorker.TITLE_KEY,
                        context.getString(R.string.work_one_time_notification_title)
                    )
                    .build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            ONE_TIME_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelOneTimeReminder() {
        workManager.cancelUniqueWork(ONE_TIME_WORK_NAME)
    }
}
