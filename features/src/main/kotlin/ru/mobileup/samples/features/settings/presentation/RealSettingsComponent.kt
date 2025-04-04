package ru.mobileup.samples.features.settings.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.app_settings.data.AppSettingsStorage
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.utils.componentScope

class RealSettingsComponent(
    componentContext: ComponentContext,
    private val appSettingsStorage: AppSettingsStorage,
    private val errorHandler: ErrorHandler,
) : ComponentContext by componentContext, SettingsComponent {

    override val settings: StateFlow<AppSettingsState> = appSettingsStorage.settings

    override fun onThemeClick(theme: AppTheme) {
        componentScope.safeLaunch(errorHandler) {
            appSettingsStorage.setTheme(theme)
        }
    }
}
