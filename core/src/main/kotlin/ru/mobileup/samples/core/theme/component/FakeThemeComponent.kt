package ru.mobileup.samples.core.theme.component

import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.theme.AppTheme

class FakeThemeComponent : ThemeComponent {

    override val theme = MutableStateFlow(AppTheme.System)
}
