package ru.mobileup.samples.core.app_settings.data

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.theme.AppTheme

interface AppSettingsStorage {

    val settings: StateFlow<AppSettingsState>

    suspend fun getTheme(): AppTheme

    suspend fun setTheme(theme: AppTheme)
}
