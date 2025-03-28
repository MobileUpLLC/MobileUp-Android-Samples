package ru.mobileup.samples.core.biometric.data

import ru.mobileup.samples.core.biometric.domain.BiometricEnableStatus
import ru.mobileup.samples.core.settings.SettingsFactory

class BiometricEnablingStorageImpl(settingsFactory: SettingsFactory) : BiometricEnablingStorage {

    companion object {
        private const val KEY_BIOMETRIC_ENABLED = "key_biometric_enabled"
    }

    private val settings = settingsFactory.createEncryptedSettings("biometric")

    override suspend fun putBiometricEnableStatus(
        biometricEnableStatus: BiometricEnableStatus
    ) {
        settings.putInt(
            KEY_BIOMETRIC_ENABLED,
            biometricEnableStatus.mapToInt()
        )
    }

    override suspend fun getBiometricEnableStatus(): BiometricEnableStatus {
        return settings
            .getInt(KEY_BIOMETRIC_ENABLED)
            ?.mapToBiometricEnableStatus()
            ?: return BiometricEnableStatus.Unknown
    }
}

private fun BiometricEnableStatus.mapToInt() = when (this) {
    BiometricEnableStatus.Enabled -> 0
    BiometricEnableStatus.Disabled -> 1
    BiometricEnableStatus.Unknown -> 2
}

private fun Int.mapToBiometricEnableStatus() = when (this) {
    0 -> BiometricEnableStatus.Enabled
    1 -> BiometricEnableStatus.Disabled
    else -> BiometricEnableStatus.Unknown
}
