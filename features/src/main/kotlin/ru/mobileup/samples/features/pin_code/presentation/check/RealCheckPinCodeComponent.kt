package ru.mobileup.samples.features.pin_code.presentation.check

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnResume
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.biometric.data.BiometricEnablingStorage
import ru.mobileup.samples.core.biometric.data.BiometricService
import ru.mobileup.samples.core.biometric.domain.BiometricAuthResult
import ru.mobileup.samples.core.biometric.domain.BiometricEnableStatus
import ru.mobileup.samples.core.biometric.domain.BiometricSupportStatus
import ru.mobileup.samples.core.dialog.standard.DialogButton
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.dialog.standard.StandardDialogData
import ru.mobileup.samples.core.dialog.standard.standardDialogControl
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.core.utils.timeNow
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.pin_code.data.PinCodeStorage
import ru.mobileup.samples.features.pin_code.domain.PinCode
import ru.mobileup.samples.features.pin_code.domain.PinCode.Companion.COUNT_TOO_MANY_ATTEMPTS
import ru.mobileup.samples.features.pin_code.domain.PinCode.Companion.PIN_CODE_LOCK_TIME
import ru.mobileup.samples.features.pin_code.domain.PinCodeProgressState
import ru.mobileup.samples.core.R as CoreR

class RealCheckPinCodeComponent(
    componentContext: ComponentContext,
    private val pinCodeStorage: PinCodeStorage,
    private val biometricService: BiometricService,
    private val biometricEnablingStorage: BiometricEnablingStorage,
    private val errorHandler: ErrorHandler,
    private val messageService: MessageService,
    private val onOutput: (CheckPinCodeComponent.Output) -> Unit
) : ComponentContext by componentContext, CheckPinCodeComponent {

    private var pinCode = ""

    override val pinProgressState = MutableStateFlow<PinCodeProgressState>(
        PinCodeProgressState.Progress(0)
    )

    private val isBiometricsSupported = MutableStateFlow(false)

    override val isErrorText = MutableStateFlow(false)

    private val timerDialogData = StandardDialogData(
        title = R.string.pin_code_alert_error_header.strResDesc(),
        message = R.string.pin_code_alert_error_text.strResDesc(),
        confirmButton = DialogButton(
            text = CoreR.string.common_ok.strResDesc(),
            action = ::onDialogDismiss
        )
    )

    private val logoutDialogData = StandardDialogData(
        title = R.string.pin_code_alert_logout_header.strResDesc(),
        message = R.string.pin_code_alert_logout_text.strResDesc(),
        confirmButton = DialogButton(
            text = CoreR.string.common_yes.strResDesc(),
            action = ::onLogoutConfirmed
        ),
        dismissButton = DialogButton(
            text = CoreR.string.common_no.strResDesc(),
            action = ::onDialogDismiss
        )
    )

    override val endButtonState: StateFlow<CheckPinCodeComponent.EndButtonState> =
        computed(
            pinProgressState,
            isBiometricsSupported
        ) { pinProgressState, isBiometricsSupported ->
            when {
                pinProgressState is PinCodeProgressState.Progress && pinProgressState.count > 0 -> {
                    CheckPinCodeComponent.EndButtonState.Erase
                }

                isBiometricsSupported -> CheckPinCodeComponent.EndButtonState.Biometrics
                else -> CheckPinCodeComponent.EndButtonState.None
            }
        }

    override val dialogControl: StandardDialogControl = standardDialogControl("dialogControl")

    private fun startFingerprintAuth() = biometricService.startBiometricAuth(
        title = CoreR.string.biometric_prompt_title.strResDesc(),
        description = CoreR.string.biometric_prompt_description.strResDesc(),
    ) {
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
            if (shouldShowTimerDialog()) {
                dialogControl.show(timerDialogData)
            } else {
                when {
                    pinProgressState.value is PinCodeProgressState.Error -> {
                        pinCode = digit.toString()
                        updateProgressState()
                    }

                    pinCode.length != PinCode.LENGTH -> {
                        pinCode += digit
                        if (pinCode.length == PinCode.LENGTH) {
                            validatePinCode()
                        } else {
                            updateProgressState()
                        }
                    }
                }
            }
        }
    }

    override fun onEraseClick() {
        pinCode = pinCode.dropLast(1)
        updateProgressState()
    }

    override fun onPinCodeInputAnimationEnd() {
        when (pinProgressState.value) {
            PinCodeProgressState.Error -> {
                isErrorText.value = true
                updateProgressState()
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

    override fun onLogoutClick() {
        dialogControl.show(logoutDialogData)
    }

    override fun onBiometricClick() {
        startFingerprintAuth()
    }

    private fun updateProgressState() {
        pinProgressState.update { PinCodeProgressState.Progress(pinCode.length) }
    }

    private fun validatePinCode() {
        componentScope.launch {
            val pinCodeFromStorage = pinCodeStorage.getPinCode()?.value
                ?: onOutput(CheckPinCodeComponent.Output.CheckSucceeded)

            if (pinCode == pinCodeFromStorage) {
                pinProgressState.update { PinCodeProgressState.Success }
            } else {
                pinCodeStorage.incrementAttemptsCounter()
                if (pinCodeStorage.getAttemptsCounter() == COUNT_TOO_MANY_ATTEMPTS) {
                    pinCodeStorage.setAttemptsCounter(0)
                    pinCodeStorage.setBadAuthTimestamp(timeNow().toEpochMilliseconds())
                    dialogControl.show(timerDialogData)
                }
                pinCode = ""
                pinProgressState.update { PinCodeProgressState.Error }
            }
        }
    }

    private fun onLogoutConfirmed() {
        // do nothing
    }

    private fun onDialogDismiss() {
        dialogControl.dismiss()
    }

    private suspend fun shouldShowTimerDialog(): Boolean {
        val now = timeNow().toEpochMilliseconds()
        val pinCodeUnlockTime = pinCodeStorage.getBadAuthTimestamp() + PIN_CODE_LOCK_TIME
        return now < pinCodeUnlockTime
    }

    init {
        backHandler.register(BackCallback {})

        componentScope.safeLaunch(errorHandler) {
            val isBiometricsSupportedOnDevice =
                biometricService.getBiometricSupportStatus() == BiometricSupportStatus.Supported

            val isBiometricsEnabled =
                biometricEnablingStorage.getBiometricEnableStatus() == BiometricEnableStatus.Enabled

            if (isBiometricsSupportedOnDevice && isBiometricsEnabled) {
                isBiometricsSupported.value = true
                startFingerprintAuth()
            }
        }

        lifecycle.doOnResume {
            componentScope.launch {
                if (shouldShowTimerDialog()) {
                    dialogControl.show(timerDialogData)
                }
            }
        }
    }
}
