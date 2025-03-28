package ru.mobileup.samples.features.pin_code.presentation.check

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.biometric.domain.BiometricType
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

interface CheckPinCodeComponent {
    val pinProgressState: StateFlow<PinCodeProgressState>
    val isError: StateFlow<Boolean>
    val isBiometricsSupported: StateFlow<Boolean>
    val isLogoutDialogVisible: StateFlow<Boolean>
    val isTimerDialogVisible: StateFlow<Boolean>
    val biometricType: BiometricType

    fun onDigitClick(digit: Int)

    fun onEraseClick()

    fun onPinCodeInputAnimationEnd()

    fun onBiometricClick()

    fun onLogoutDialogVisibilityChange()

    fun onLogoutConfirmed()

    fun onDialogDismiss()

    sealed interface Output {
        data object CheckSucceeded : Output
        data object LoggedOut : Output
    }
}
