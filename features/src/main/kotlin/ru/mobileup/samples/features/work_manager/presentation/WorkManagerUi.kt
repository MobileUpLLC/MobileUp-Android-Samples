package ru.mobileup.samples.features.work_manager.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.work_manager.presentation.model.DelayTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.IntervalTimeUnit
import ru.mobileup.samples.features.work_manager.presentation.model.TaskType
import ru.mobileup.samples.features.work_manager.presentation.widget.NextExecutionTimeInfo
import ru.mobileup.samples.features.work_manager.presentation.widget.TaskControlButtons
import ru.mobileup.samples.features.work_manager.presentation.widget.TimeSlider
import ru.mobileup.samples.features.work_manager.presentation.widget.WorkSampleInfoDialog

@Composable
fun WorkManagerUi(
    component: WorkManagerComponent,
    modifier: Modifier = Modifier,
) {
    val taskType by component.taskType.collectAsState()
    val tabs = remember(TaskType::entries)
    var showInfo by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 8.dp, top = 8.dp)
                .clip(CircleShape)
                .clickable { showInfo = !showInfo }
                .padding(8.dp),
            imageVector = Icons.Outlined.Info,
            contentDescription = null
        )

        SecondaryTabRow(
            selectedTabIndex = tabs.indexOf(taskType),
        ) {
            tabs.forEach { type ->
                Tab(
                    selected = taskType == type,
                    onClick = { component.onTaskTypeChange(type) },
                    text = {
                        Text(
                            text = stringResource(type.resId),
                            style = CustomTheme.typography.title.regular,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        when (taskType) {
            TaskType.PERIODIC -> PeriodicTaskSettings(component)
            TaskType.ONE_TIME -> OneTimeTaskSettings(component)
        }
    }

    WorkSampleInfoDialog(
        showDialog = showInfo,
        onDismiss = { showInfo = false }
    )
}

@Composable
private fun PeriodicTaskSettings(
    component: WorkManagerComponent,
    modifier: Modifier = Modifier,
) {
    val interval by component.periodicInterval.collectAsState()
    val intervalUnit by component.periodicIntervalTimeUnit.collectAsState()

    val delay by component.periodicInitialDelay.collectAsState()
    val delayUnit by component.periodicInitialDelayTimeUnit.collectAsState()

    val isScheduled by component.isPeriodicScheduled.collectAsState()
    val nextExecutionTime by component.nextPeriodicExecutionTime.collectAsState()

    val scrollState = rememberScrollState()

    Column(modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            TimeSlider(
                label = stringResource(R.string.work_interval),
                value = interval,
                unit = intervalUnit,
                onValueChange = component::onIntervalChange,
                onUnitChange = component::onIntervalTimeUnitChange,
                allUnits = IntervalTimeUnit.entries,
                getRange = IntervalTimeUnit::range,
                getPluralResId = IntervalTimeUnit::pluralResId,
                getResId = IntervalTimeUnit::resId
            )

            Spacer(Modifier.height(12.dp))

            TimeSlider(
                label = stringResource(R.string.work_initial_delay),
                value = delay,
                unit = delayUnit,
                onValueChange = component::onInitialDelayChange,
                onUnitChange = component::onInitialDelayTimeUnitChange,
                allUnits = DelayTimeUnit.entries,
                getRange = DelayTimeUnit::range,
                getPluralResId = DelayTimeUnit::pluralResId,
                getResId = DelayTimeUnit::resId
            )

            Spacer(Modifier.height(16.dp))

            NextExecutionTimeInfo(nextExecutionTime)
        }

        Spacer(Modifier.weight(1f))

        TaskControlButtons(
            modifier = Modifier.padding(16.dp),
            isScheduled = isScheduled,
            onStartClick = component::onStartClick,
            onCancelClick = component::onCancelClick,
        )
    }
}

@Composable
private fun OneTimeTaskSettings(
    component: WorkManagerComponent,
    modifier: Modifier = Modifier,
) {
    val delay by component.oneTimeInitialDelay.collectAsState()
    val delayUnit by component.oneTimeInitialDelayTimeUnit.collectAsState()
    val isScheduled by component.isOneTimeScheduled.collectAsState()
    val executionTime by component.oneTimeExecutionTime.collectAsState()

    val scrollState = rememberScrollState()

    Column(modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            TimeSlider(
                label = stringResource(R.string.work_initial_delay),
                value = delay,
                unit = delayUnit,
                onValueChange = component::onInitialDelayChange,
                onUnitChange = component::onInitialDelayTimeUnitChange,
                allUnits = DelayTimeUnit.entries,
                getRange = DelayTimeUnit::range,
                getPluralResId = DelayTimeUnit::pluralResId,
                getResId = DelayTimeUnit::resId
            )

            Spacer(Modifier.height(16.dp))

            NextExecutionTimeInfo(executionTime)
        }

        Spacer(Modifier.weight(1f))

        TaskControlButtons(
            modifier = Modifier.padding(16.dp),
            isScheduled = isScheduled,
            onStartClick = component::onStartClick,
            onCancelClick = component::onCancelClick,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkManagerPreview() {
    AppTheme {
        WorkManagerUi(FakeWorkManagerComponent())
    }
}
