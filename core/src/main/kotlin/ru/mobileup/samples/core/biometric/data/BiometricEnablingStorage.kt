package ru.mobileup.samples.core.biometric.data

import ru.mobileup.samples.core.biometric.domain.BiometricEnableStatus

interface BiometricEnablingStorage {

    suspend fun putBiometricEnableStatus(biometricEnableStatus: BiometricEnableStatus)
    suspend fun getBiometricEnableStatus(): BiometricEnableStatus
}
