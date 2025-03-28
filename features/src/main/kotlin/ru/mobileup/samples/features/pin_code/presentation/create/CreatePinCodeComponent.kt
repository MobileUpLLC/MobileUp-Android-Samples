package ru.mobileup.samples.features.pin_code.presentation.create

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

interface CreatePinCodeComponent {

    val pinProgressState: StateFlow<PinCodeProgressState>
    val pinInputStep: StateFlow<PinInputStep>
    val isEraseButtonAvailable: StateFlow<Boolean>
    val dialogControl: StandardDialogControl

    fun onDigitClick(digit: Int)
    fun onEraseClick()
    fun onPinCodeInputAnimationEnd()
    fun onDialogBiometricEnableClick()
    fun onDialogSettingsClick()
    fun onDialogCancelClick()

    sealed interface Output {
        data object PinCodeSet : Output
    }

    sealed interface PinInputStep {
        data object None : PinInputStep
        data object Repeat : PinInputStep
        data object PreviouslyErred : PinInputStep
    }
}
