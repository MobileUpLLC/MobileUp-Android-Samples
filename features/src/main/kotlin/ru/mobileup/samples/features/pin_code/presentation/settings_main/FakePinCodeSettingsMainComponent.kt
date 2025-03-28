package ru.mobileup.samples.features.pin_code.presentation.settings_main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakePinCodeSettingsMainComponent : PinCodeSettingsMainComponent {
    override val isPinCodeEnabled: StateFlow<Boolean> = MutableStateFlow(false)
    override val isBiometricEnabled: StateFlow<Boolean> = MutableStateFlow(false)

    override fun onPinCodeEnabledChanged(isEnabled: Boolean) = Unit

    override fun onBiometricEnabledChanged(isEnabled: Boolean) = Unit
}