package ru.mobileup.samples.features.work_manager.presentation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.permissions.SinglePermissionResult
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.features.work_manager.data.ReminderScheduler
import ru.mobileup.samples.features.work_manager.presentation.model.DelayTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.IntervalTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.TaskType
import java.time.Duration

class RealWorkManagerComponent(
    componentContext: ComponentContext,
    private val reminderScheduler: ReminderScheduler,
    private val permissionService: PermissionService,
) : ComponentContext by componentContext, WorkManagerComponent {

    override val taskType = MutableStateFlow(TaskType.PERIODIC)

    private val lastPeriodicWork = reminderScheduler.periodicReminderState.map {
        it.lastOrNull { !it.state.isFinished }
    }.stateIn(componentScope, SharingStarted.Eagerly, null)

    override val isPeriodicScheduled = computed(lastPeriodicWork) { it != null }

    override val nextPeriodicExecutionTime = computed(lastPeriodicWork) {
        it?.nextScheduleTimeMillis
    }

    override val periodicIntervalTimeUnit = MutableStateFlow(IntervalTimeUnit.MINUTES)

    override val periodicInterval = MutableStateFlow(periodicIntervalTimeUnit.value.range.first)

    override val periodicInitialDelayTimeUnit = MutableStateFlow(DelayTimeUnit.SECONDS)

    override val periodicInitialDelay =
        MutableStateFlow(periodicInitialDelayTimeUnit.value.range.first)

    private val lastOneTimeTask = reminderScheduler.oneTimeReminderState.map {
        it.lastOrNull { !it.state.isFinished }
    }.stateIn(componentScope, SharingStarted.Eagerly, null)

    override val isOneTimeScheduled = computed(lastOneTimeTask) { it != null }

    override val oneTimeExecutionTime = computed(lastOneTimeTask) {
        it?.nextScheduleTimeMillis
    }

    override val oneTimeInitialDelayTimeUnit = MutableStateFlow(DelayTimeUnit.SECONDS)

    override val oneTimeInitialDelay =
        MutableStateFlow(periodicInitialDelayTimeUnit.value.range.first)

    init {
        initPeriodicInfo()
        initOneTimeInfo()
    }

    private fun initPeriodicInfo() {
        lastPeriodicWork
            .filterNotNull()
            .onEach { info ->
                info.initialDelayMillis.mapMillisToDelayUi().let { (unit, value) ->
                    periodicInitialDelayTimeUnit.value = unit
                    periodicInitialDelay.value = value
                }
            }
            .mapNotNull { info -> info.periodicityInfo?.repeatIntervalMillis }
            .onEach { repeatIntervalMillis ->
                repeatIntervalMillis.mapMillisToIntervalUi().let { (unit, value) ->
                    periodicIntervalTimeUnit.value = unit
                    periodicInterval.value = value
                }
            }
            .launchIn(componentScope)
    }

    private fun initOneTimeInfo() {
        lastOneTimeTask
            .filterNotNull()
            .onEach { info ->
                info.initialDelayMillis.mapMillisToDelayUi().let { (unit, value) ->
                    oneTimeInitialDelayTimeUnit.value = unit
                    oneTimeInitialDelay.value = value
                }
            }
            .launchIn(componentScope)
    }

    override fun onTaskTypeChange(type: TaskType) {
        taskType.value = type
    }

    override fun onIntervalChange(value: Long) {
        periodicInterval.value = value
    }

    override fun onInitialDelayChange(value: Long) {
        when (taskType.value) {
            TaskType.PERIODIC -> periodicInitialDelay.value = value
            TaskType.ONE_TIME -> oneTimeInitialDelay.value = value
        }
    }

    override fun onIntervalTimeUnitChange(unit: IntervalTimeUnit) {
        periodicIntervalTimeUnit.value = unit
        periodicInterval.value = unit.range.first
    }

    override fun onInitialDelayTimeUnitChange(unit: DelayTimeUnit) {
        when (taskType.value) {
            TaskType.PERIODIC -> {
                periodicInitialDelayTimeUnit.value = unit
                periodicInitialDelay.value = unit.range.first
            }

            TaskType.ONE_TIME -> {
                oneTimeInitialDelayTimeUnit.value = unit
                oneTimeInitialDelay.value = unit.range.first
            }
        }
    }

    override fun onStartClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionIfNeeded { isGranted ->
                if (isGranted) scheduleReminder()
            }
        } else {
            scheduleReminder()
        }
    }

    private fun scheduleReminder() = when (taskType.value) {
        TaskType.PERIODIC -> schedulePeriodicReminder()
        TaskType.ONE_TIME -> scheduleOneTimeReminder()
    }

    private fun schedulePeriodicReminder() = reminderScheduler.schedulePeriodicReminder(
        interval = when (periodicIntervalTimeUnit.value) {
            IntervalTimeUnit.MINUTES -> Duration.ofMinutes(periodicInterval.value)
            IntervalTimeUnit.HOURS -> Duration.ofHours(periodicInterval.value)
            IntervalTimeUnit.DAYS -> Duration.ofDays(periodicInterval.value)
        },
        initialDelay = when (periodicInitialDelayTimeUnit.value) {
            DelayTimeUnit.SECONDS -> Duration.ofSeconds(periodicInitialDelay.value)
            DelayTimeUnit.MINUTES -> Duration.ofMinutes(periodicInitialDelay.value)
            DelayTimeUnit.HOURS -> Duration.ofHours(periodicInitialDelay.value)
            DelayTimeUnit.DAYS -> Duration.ofDays(periodicInitialDelay.value)
        }
    )

    private fun scheduleOneTimeReminder() = reminderScheduler.scheduleOneTimeReminder(
        initialDelay = when (oneTimeInitialDelayTimeUnit.value) {
            DelayTimeUnit.SECONDS -> Duration.ofSeconds(oneTimeInitialDelay.value)
            DelayTimeUnit.MINUTES -> Duration.ofMinutes(oneTimeInitialDelay.value)
            DelayTimeUnit.HOURS -> Duration.ofHours(oneTimeInitialDelay.value)
            DelayTimeUnit.DAYS -> Duration.ofDays(oneTimeInitialDelay.value)
        }
    )

    override fun onCancelClick() = when (taskType.value) {
        TaskType.PERIODIC -> reminderScheduler.cancelPeriodicReminder()
        TaskType.ONE_TIME -> reminderScheduler.cancelOneTimeReminder()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermissionIfNeeded(onResult: (Boolean) -> Unit) {
        if (permissionService.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
            onResult(true)
        } else {
            componentScope.launch {
                val result =
                    permissionService.requestPermission(Manifest.permission.POST_NOTIFICATIONS)

                onResult(result is SinglePermissionResult.Granted)
            }
        }
    }
}

private fun Long.mapMillisToDelayUi(): Pair<DelayTimeUnit, Long> {
    DelayTimeUnit.entries.forEach { unit ->
        when (unit) {
            DelayTimeUnit.SECONDS -> this / 1_000
            DelayTimeUnit.MINUTES -> this / 60_000
            DelayTimeUnit.HOURS -> this / 3_600_000
            DelayTimeUnit.DAYS -> this / 86_400_000
        }.let {
            if (it in unit.range) return unit to it
        }
    }

    return DelayTimeUnit.SECONDS to (this / 1_000)
}

private fun Long.mapMillisToIntervalUi(): Pair<IntervalTimeUnit, Long> {
    IntervalTimeUnit.entries.forEach { unit ->
        when (unit) {
            IntervalTimeUnit.MINUTES -> this / 60_000
            IntervalTimeUnit.HOURS -> this / 3_600_000
            IntervalTimeUnit.DAYS -> this / 86_400_000
        }.let {
            if (it in unit.range) return unit to it
        }
    }

    return IntervalTimeUnit.MINUTES to (this / 60_000)
}
