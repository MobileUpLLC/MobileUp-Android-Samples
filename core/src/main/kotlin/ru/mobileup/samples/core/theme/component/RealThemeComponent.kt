package ru.mobileup.samples.core.theme.component

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.app_settings.data.AppSettingsStorage
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.utils.computed

class RealThemeComponent(
    componentContext: ComponentContext,
    appSettingsStorage: AppSettingsStorage,
) : ComponentContext by componentContext, ThemeComponent {

    override val theme: StateFlow<AppTheme> = computed(appSettingsStorage.settings) {
        when (it) {
            is AppSettingsState.Initialized -> it.value.theme
            AppSettingsState.Uninitialized -> AppTheme.System
        }
    }
}
