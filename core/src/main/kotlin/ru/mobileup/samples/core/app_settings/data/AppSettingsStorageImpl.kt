package ru.mobileup.samples.core.app_settings.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import ru.mobileup.samples.core.app_settings.domain.AppSettings
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.settings.SettingsFactory
import ru.mobileup.samples.core.theme.AppTheme

class AppSettingsStorageImpl(
    settingsFactory: SettingsFactory,
) : AppSettingsStorage {

    companion object {
        private const val SETTINGS_NAME = "user_settings"
        private const val KEY_THEME = "user_settings_theme"
    }

    private val settingsPrefs = settingsFactory.createSettings(SETTINGS_NAME)

    override val settings = MutableStateFlow<AppSettingsState>(AppSettingsState.Uninitialized)

    init {
        initSettings()
    }

    private fun initSettings() = runBlocking {
        settings.value = AppSettingsState.Initialized(
            AppSettings(
                theme = getTheme()
            )
        )
    }

    override suspend fun getTheme(): AppTheme = settingsPrefs.getString(KEY_THEME)?.let {
        runCatching { AppTheme.valueOf(it) }.getOrElse { AppTheme.System }
    } ?: AppTheme.System

    override suspend fun setTheme(theme: AppTheme) = settingsPrefs.putString(
        KEY_THEME, theme.name
    ).also {
        updateSettingsState { it.copy(theme = theme) }
    }

    private fun updateSettingsState(update: (AppSettings) -> AppSettings) = synchronized(this) {
        val updatedSettings = when (val currentState = settings.value) {
            is AppSettingsState.Initialized -> {
                val updatedValue = update(currentState.value)
                currentState.copy(value = updatedValue)
            }

            AppSettingsState.Uninitialized -> {
                AppSettingsState.Initialized(update(AppSettings()))
            }
        }
        settings.value = updatedSettings
    }
}
