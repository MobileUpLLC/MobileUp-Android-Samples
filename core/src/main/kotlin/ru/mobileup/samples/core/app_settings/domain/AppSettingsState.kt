package ru.mobileup.samples.core.app_settings.domain

sealed interface AppSettingsState {
    data class Initialized(val value: AppSettings) : AppSettingsState
    data object Uninitialized : AppSettingsState
}
