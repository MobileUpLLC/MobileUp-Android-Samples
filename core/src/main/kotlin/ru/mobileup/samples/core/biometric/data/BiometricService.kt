package ru.mobileup.samples.core.biometric.data

import ru.mobileup.samples.core.biometric.domain.BiometricAuthResult
import ru.mobileup.samples.core.biometric.domain.BiometricSupportStatus
import ru.mobileup.samples.core.biometric.domain.BiometricType

interface BiometricService {

    val biometricType: BiometricType

    fun startBiometricAuth(callback: (authStatus: BiometricAuthResult) -> Unit)

    fun getBiometricSupportStatus(): BiometricSupportStatus
}
