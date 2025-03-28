package ru.mobileup.samples.features.pin_code.presentation.settings_main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.biometric.data.BiometricEnablingStorage
import ru.mobileup.samples.core.biometric.domain.BiometricEnableStatus
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.pin_code.data.PinCodeStorage

class RealPinCodeSettingsMainComponent(
    componentContext: ComponentContext,
    private val onOutput: (PinCodeSettingsMainComponent.Output) -> Unit,
    private val pinCodeStorage: PinCodeStorage,
    private val biometricEnablingStorage: BiometricEnablingStorage,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, PinCodeSettingsMainComponent {

    override val isPinCodeEnabled: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    override val isBiometricEnabled: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    init {
        lifecycle.doOnStart {
            componentScope.safeLaunch(errorHandler) {
                isPinCodeEnabled.value = pinCodeStorage.getPinCode() != null
                isBiometricEnabled.value = biometricEnablingStorage.getBiometricEnableStatus() == BiometricEnableStatus.Enabled
            }
        }
    }

    override fun onPinCodeEnabledChanged(isEnabled: Boolean) {
        if (isEnabled) {
            onOutput(PinCodeSettingsMainComponent.Output.CreatePinCodeRequested)
        } else {
            componentScope.safeLaunch(errorHandler) {
                pinCodeStorage.deletePinCode()
                biometricEnablingStorage.putBiometricEnableStatus(BiometricEnableStatus.Disabled)
                isPinCodeEnabled.value = false
                isBiometricEnabled.value = false
            }
        }
    }

    override fun onBiometricEnabledChanged(isEnabled: Boolean) {
        isBiometricEnabled.update { isEnabled }
        val status = if (isEnabled) {
            BiometricEnableStatus.Enabled
        } else {
            BiometricEnableStatus.Disabled
        }
        componentScope.safeLaunch(errorHandler) {
            biometricEnablingStorage.putBiometricEnableStatus(status)
        }
    }
}