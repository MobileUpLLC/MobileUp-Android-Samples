package ru.mobileup.samples.features.work_manager.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.work_manager.presentation.model.DelayTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.IntervalTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.TaskType

interface WorkManagerComponent {

    val taskType: StateFlow<TaskType>

    val isPeriodicScheduled: StateFlow<Boolean>
    val nextPeriodicExecutionTime: StateFlow<Long?>

    val periodicIntervalTimeUnit: StateFlow<IntervalTimeUnit>
    val periodicInterval: StateFlow<Long>

    val periodicInitialDelayTimeUnit: StateFlow<DelayTimeUnit>
    val periodicInitialDelay: StateFlow<Long>

    val isOneTimeScheduled: StateFlow<Boolean>
    val oneTimeExecutionTime: StateFlow<Long?>

    val oneTimeInitialDelayTimeUnit: StateFlow<DelayTimeUnit>
    val oneTimeInitialDelay: StateFlow<Long>

    fun onTaskTypeChange(type: TaskType)

    fun onIntervalChange(value: Long)
    fun onInitialDelayChange(value: Long)
    fun onIntervalTimeUnitChange(unit: IntervalTimeUnit)
    fun onInitialDelayTimeUnitChange(unit: DelayTimeUnit)

    fun onStartClick()
    fun onCancelClick()
}
