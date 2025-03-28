package ru.mobileup.samples.features.pin_code.presentation.settings_main

import kotlinx.coroutines.flow.StateFlow

interface PinCodeSettingsMainComponent {

    val isPinCodeEnabled: StateFlow<Boolean>

    val isBiometricEnabled: StateFlow<Boolean>

    fun onPinCodeEnabledChanged(isEnabled: Boolean)

    fun onBiometricEnabledChanged(isEnabled: Boolean)

    sealed interface Output {
        data object CreatePinCodeRequested : Output
    }
}