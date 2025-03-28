package ru.mobileup.samples.features.pin_code.presentation.check_management

import com.arkivanov.decompose.router.slot.ChildSlot
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildSlot
import ru.mobileup.samples.features.pin_code.presentation.check.CheckPinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.check.FakeCheckPinCodeComponent

class FakeCheckPinCodeManagementComponent : CheckPinCodeManagementComponent {
    override val checkPinCodeComponentSlot: StateFlow<ChildSlot<*, CheckPinCodeComponent>> =
        createFakeChildSlot(Unit, FakeCheckPinCodeComponent())
}