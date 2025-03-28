package ru.mobileup.samples.features.pin_code.data

import ru.mobileup.samples.features.pin_code.domain.PinCode

interface PinCodeStorage {

    suspend fun getBadAuthTimestamp(): Long

    suspend fun setBadAuthTimestamp(timestamp: Long)

    suspend fun getAttemptsCounter(): Int

    suspend fun setAttemptsCounter(counter: Int)

    suspend fun incrementAttemptsCounter()

    suspend fun savePinCode(pinCode: PinCode)

    suspend fun getPinCode(): PinCode?

    suspend fun deletePinCode()
}
