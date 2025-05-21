package ru.mobileup.samples.features.work_manager.data

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface ReminderScheduler {

    val periodicReminderState: Flow<List<WorkInfo>>

    fun schedulePeriodicReminder(interval: Duration, initialDelay: Duration = Duration.ZERO)

    fun cancelPeriodicReminder()

    val oneTimeReminderState: Flow<List<WorkInfo>>

    fun scheduleOneTimeReminder(initialDelay: Duration = Duration.ZERO)

    fun cancelOneTimeReminder()
}
