package ru.mobileup.samples.features.settings.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.app_settings.domain.AppSettings
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.theme.AppTheme

class FakeSettingsComponent : SettingsComponent {

    override val settings = MutableStateFlow(AppSettingsState.Initialized(AppSettings()))

    override fun onThemeClick(theme: AppTheme) = Unit
}
