package ru.mobileup.samples.features.pin_code.presentation.check_management

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.mobileup.samples.features.pin_code.presentation.check.CheckPinCodeUi

@Composable
fun CheckPinCodeManagementUi(
    component: CheckPinCodeManagementComponent,
    modifier: Modifier = Modifier
) {
    val slot by component.checkPinCodeComponentSlot.collectAsState()
    val childComponent = slot.child?.instance
    if (childComponent != null) {
        CheckPinCodeUi(childComponent, modifier)
    }
}