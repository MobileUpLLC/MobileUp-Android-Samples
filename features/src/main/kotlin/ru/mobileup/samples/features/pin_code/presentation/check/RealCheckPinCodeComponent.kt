package ru.mobileup.samples.features.pin_code.presentation.check

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnResume
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import ru.mobileup.samples.core.biometric.data.BiometricEnablingStorage
import ru.mobileup.samples.core.biometric.data.BiometricService
import ru.mobileup.samples.core.biometric.domain.BiometricAuthResult
import ru.mobileup.samples.core.biometric.domain.BiometricEnableStatus
import ru.mobileup.samples.core.biometric.domain.BiometricType
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.pin_code.data.PinCodeStorage
import ru.mobileup.samples.features.pin_code.domain.PinCode
import ru.mobileup.samples.features.pin_code.domain.PinCode.Companion.COUNT_TOO_MANY_ATTEMPTS
import ru.mobileup.samples.features.pin_code.domain.PinCode.Companion.PIN_CODE_LOCK_TIME
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState
import kotlin.math.max

class RealCheckPinCodeComponent(
    componentContext: ComponentContext,
    private val pinCodeStorage: PinCodeStorage,
    private val biometricService: BiometricService,
    private val biometricEnablingStorage: BiometricEnablingStorage,
    private val errorHandler: ErrorHandler,
    private val messageService: MessageService,
    private val onOutput: (CheckPinCodeComponent.Output) -> Unit
) : ComponentContext by componentContext, CheckPinCodeComponent {

    private var pinCode = MutableStateFlow("")

    override var isLogoutDialogVisible = MutableStateFlow(false)
    override val biometricType: BiometricType = biometricService.biometricType

    override val pinProgressState = MutableStateFlow<PinCodeProgressState>(
        PinCodeProgressState.Progress(0)
    )

    override val isBiometricsSupported = MutableStateFlow(false)

    override var isTimerDialogVisible = MutableStateFlow(false)

    override var isError = MutableStateFlow(false)

    override fun onLogoutDialogVisibilityChange() {
        isLogoutDialogVisible.update { !it }
    }

    override fun onLogoutConfirmed() {
        componentScope.safeLaunch(errorHandler) {
            onOutput(CheckPinCodeComponent.Output.LoggedOut)
        }
    }

    override fun onDialogDismiss() {
        isTimerDialogVisible.update { false }
    }

    private fun startFingerprintAuth() = biometricService.startBiometricAuth {
        when (it) {
            BiometricAuthResult.Success -> {
                componentScope.launch {
                    pinCodeStorage.setAttemptsCounter(0)
                    pinCodeStorage.setBadAuthTimestamp(0)
                    biometricEnablingStorage.putBiometricEnableStatus(
                        BiometricEnableStatus.Enabled
                    )
                    onOutput(CheckPinCodeComponent.Output.CheckSucceeded)
                }
            }

            BiometricAuthResult.Cancel -> {}
            BiometricAuthResult.Failed -> {}
            BiometricAuthResult.TooManyAttempts -> {
                messageService.showMessage(
                    Message(R.string.biometric_too_many_attempts.strResDesc())
                )
            }
        }
    }

    override fun onDigitClick(digit: Int) {
        componentScope.launch {
            if (pinCodeStorage.getBadAuthTimestamp() >= Clock.System.now().toEpochMilliseconds() - PIN_CODE_LOCK_TIME) {
                isTimerDialogVisible.value = true
            } else {
                when {
                    pinProgressState.value is PinCodeProgressState.Error -> {
                        pinCode.value = digit.toString()
                        updateProgress()
                    }

                    pinCode.value.length != PinCode.LENGTH -> {
                        pinCode.value += digit
                        updateProgress()
                        validatePinCode()
                    }
                }
            }
        }
    }

    override fun onEraseClick() {
        pinCode.update { it.take(max(it.length - 1, 0)) }
        updateProgress()
    }

    override fun onPinCodeInputAnimationEnd() {
        when (pinProgressState.value) {
            PinCodeProgressState.Error -> {
                updateProgress()
            }

            PinCodeProgressState.Success -> {
                componentScope.safeLaunch(errorHandler) {
                    pinCodeStorage.setAttemptsCounter(0)
                    pinCodeStorage.setBadAuthTimestamp(0)
                    onOutput(CheckPinCodeComponent.Output.CheckSucceeded)
                }
            }

            is PinCodeProgressState.Progress -> {}
        }
    }

    override fun onBiometricClick() {
        startFingerprintAuth()
    }

    private fun updateProgress() {
        pinProgressState.update { PinCodeProgressState.Progress(pinCode.value.length) }
    }

    private fun validatePinCode() {
        componentScope.launch {
            if (pinCode.value.length != PinCode.LENGTH) return@launch

            val pinCodeFromStorage = pinCodeStorage.getPinCode()?.value
                ?: onOutput(CheckPinCodeComponent.Output.CheckSucceeded)

            if (pinCode.value == pinCodeFromStorage) {
                pinProgressState.update { PinCodeProgressState.Success }
            } else {
                pinCodeStorage.incrementAttemptsCounter()
                if (pinCodeStorage.getAttemptsCounter() == COUNT_TOO_MANY_ATTEMPTS) {
                    pinCodeStorage.setAttemptsCounter(0)
                    pinCodeStorage.setBadAuthTimestamp(Clock.System.now().toEpochMilliseconds())
                    isTimerDialogVisible.value = true
                }
                pinCode.value = ""
                pinProgressState.update { PinCodeProgressState.Error }
            }
        }
    }

    init {
        backHandler.register(BackCallback {})

        componentScope.safeLaunch(errorHandler) {
            if (biometricEnablingStorage.getBiometricEnableStatus() == BiometricEnableStatus.Enabled) {
                isBiometricsSupported.value = true
                startFingerprintAuth()
            }
        }

        lifecycle.doOnResume {
            componentScope.launch {
                isTimerDialogVisible.update {
                    pinCodeStorage.getBadAuthTimestamp() >
                        Clock.System.now().toEpochMilliseconds() - PIN_CODE_LOCK_TIME
                }
            }
        }
    }
}
