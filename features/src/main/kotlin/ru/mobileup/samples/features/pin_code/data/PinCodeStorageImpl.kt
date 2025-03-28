package ru.mobileup.samples.features.pin_code.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mobileup.samples.core.settings.SettingsFactory
import ru.mobileup.samples.features.pin_code.domain.PinCode

class PinCodeStorageImpl(settingsFactory: SettingsFactory) : PinCodeStorage {

    private companion object {
        private const val KEY_PIN_CODE = "key_pin_code"
        private const val KEY_BAD_AUTH_TIMESTAMP = "key_bad_auth_timestamp"
        private const val KEY_ATTEMPTS_COUNTER = "key_attempts_counter"
        private const val KEY_PIN_CODE_ENABLED = "key_pin_code_enabled"
    }

    private val settings = settingsFactory.createEncryptedSettings("pin_code")

    override suspend fun getBadAuthTimestamp(): Long = withContext(Dispatchers.IO) {
        settings.getString(KEY_BAD_AUTH_TIMESTAMP)?.toLong() ?: 0
    }

    override suspend fun setBadAuthTimestamp(timestamp: Long) = withContext(Dispatchers.IO) {
        settings.putString(KEY_BAD_AUTH_TIMESTAMP, timestamp.toString())
    }

    override suspend fun getAttemptsCounter(): Int = withContext(Dispatchers.IO) {
        settings.getString(KEY_ATTEMPTS_COUNTER)?.toInt() ?: 0
    }

    override suspend fun setAttemptsCounter(counter: Int) = withContext(Dispatchers.IO) {
        settings.putString(KEY_ATTEMPTS_COUNTER, counter.toString())
    }

    override suspend fun incrementAttemptsCounter() {
        setAttemptsCounter(getAttemptsCounter() + 1)
    }

    override suspend fun savePinCode(pinCode: PinCode) = withContext(Dispatchers.IO) {
        settings.putString(KEY_PIN_CODE, pinCode.value)
    }

    override suspend fun getPinCode(): PinCode? = withContext(Dispatchers.IO) {
        val pinCodeString = settings.getString(KEY_PIN_CODE) ?: return@withContext null
        PinCode(pinCodeString)
    }

    override suspend fun deletePinCode() = withContext(Dispatchers.IO) {
        settings.remove(KEY_PIN_CODE)
    }
}
