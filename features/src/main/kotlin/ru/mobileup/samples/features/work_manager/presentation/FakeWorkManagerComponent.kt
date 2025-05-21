package ru.mobileup.samples.features.work_manager.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.work_manager.presentation.model.DelayTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.IntervalTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.TaskType

class FakeWorkManagerComponent : WorkManagerComponent {

    override val isOneTimeScheduled = MutableStateFlow(true)
    override val oneTimeExecutionTime = MutableStateFlow(System.currentTimeMillis())
    override val oneTimeInitialDelayTimeUnit = MutableStateFlow(DelayTimeUnit.SECONDS)
    override val oneTimeInitialDelay = MutableStateFlow(DelayTimeUnit.SECONDS.range.first)
    override val taskType = MutableStateFlow(TaskType.PERIODIC)
    override val isPeriodicScheduled = MutableStateFlow(true)
    override val periodicIntervalTimeUnit = MutableStateFlow(IntervalTimeUnit.MINUTES)
    override val periodicInterval = MutableStateFlow(IntervalTimeUnit.MINUTES.range.first)
    override val periodicInitialDelayTimeUnit = MutableStateFlow(DelayTimeUnit.SECONDS)
    override val periodicInitialDelay = MutableStateFlow(DelayTimeUnit.SECONDS.range.first)
    override val nextPeriodicExecutionTime = MutableStateFlow(System.currentTimeMillis())

    override fun onTaskTypeChange(type: TaskType) = Unit
    override fun onIntervalChange(value: Long) = Unit
    override fun onInitialDelayChange(value: Long) = Unit
    override fun onIntervalTimeUnitChange(unit: IntervalTimeUnit) = Unit
    override fun onInitialDelayTimeUnitChange(unit: DelayTimeUnit) = Unit
    override fun onStartClick() = Unit
    override fun onCancelClick() = Unit
}
