package ru.mobileup.samples.features.pin_code.domain

data class PinCode(val value: String) {

    companion object {
        const val LENGTH = 4
        const val COUNT_TOO_MANY_ATTEMPTS = 5
        const val PIN_CODE_LOCK_TIME = 300_000L
    }

    init {
        check(value.length == LENGTH)
    }
}

sealed interface PinCodeProgressState {
    data object Error : PinCodeProgressState
    data class Progress(val count: Int) : PinCodeProgressState
    data object Success : PinCodeProgressState
}
