package ru.mobileup.samples.features.pin_code.presentation.check

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.biometric.domain.BiometricType
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState

class FakeCheckPinCodeComponent : CheckPinCodeComponent {
    override val pinProgressState = MutableStateFlow<PinCodeProgressState>(PinCodeProgressState.Success)
    override val isError = MutableStateFlow(false)
    override val isBiometricsSupported = MutableStateFlow(false)
    override val isLogoutDialogVisible = MutableStateFlow(false)
    override val isTimerDialogVisible = MutableStateFlow(false)
    override val biometricType: BiometricType = BiometricType.FaceId

    override fun onDigitClick(digit: Int) = Unit
    override fun onEraseClick() = Unit
    override fun onPinCodeInputAnimationEnd() = Unit

    override fun onBiometricClick() = Unit
    override fun onLogoutDialogVisibilityChange() = Unit
    override fun onLogoutConfirmed() = Unit
    override fun onDialogDismiss() = Unit
}
