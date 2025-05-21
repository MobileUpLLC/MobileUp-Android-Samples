package ru.mobileup.samples.features.work_manager.presentation.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.core.R as CoreR

@Composable
fun TaskControlButtons(
    isScheduled: Boolean,
    onStartClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isScheduled) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Primary,
                text = stringResource(R.string.work_update_and_reschedule),
                onClick = onStartClick,
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Primary,
                text = stringResource(CoreR.string.common_cancel),
                onClick = onCancelClick,
            )
        }
    } else {
        AppButton(
            modifier = modifier.fillMaxWidth(),
            buttonType = ButtonType.Primary,
            text = stringResource(R.string.work_start),
            onClick = onStartClick,
        )
    }
}
