package ru.mobileup.samples.features.pin_code.presentation.create

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.dialog.standard.fakeStandardDialogControl
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

class FakeCreatePinCodeComponent : CreatePinCodeComponent {
    override val pinProgressState = MutableStateFlow<PinCodeProgressState>(PinCodeProgressState.Success)
    override val pinInputStep = MutableStateFlow<CreatePinCodeComponent.PinInputStep>(CreatePinCodeComponent.PinInputStep.None)
    override val isEraseButtonAvailable: StateFlow<Boolean> = MutableStateFlow(false)
    override val dialogControl: StandardDialogControl = fakeStandardDialogControl()

    override fun onDigitClick(digit: Int) = Unit
    override fun onEraseClick() = Unit
    override fun onPinCodeInputAnimationEnd() = Unit

    override fun onDialogBiometricEnableClick() = Unit
    override fun onDialogSettingsClick() = Unit
    override fun onDialogCancelClick() = Unit
}
