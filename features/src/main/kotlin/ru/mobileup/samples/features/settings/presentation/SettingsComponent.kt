package ru.mobileup.samples.features.settings.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.theme.AppTheme

interface SettingsComponent {

    val settings: StateFlow<AppSettingsState>

    fun onThemeClick(theme: AppTheme)
}
