package ru.mobileup.samples.features.pin_code.presentation.check_management

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.pin_code.presentation.check.CheckPinCodeComponent

interface CheckPinCodeManagementComponent {

    val checkPinCodeComponentSlot: StateFlow<ChildSlot<*, CheckPinCodeComponent>>
}