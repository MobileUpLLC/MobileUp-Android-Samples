package ru.mobileup.samples.core.app_settings.domain

import ru.mobileup.samples.core.theme.AppTheme

data class AppSettings(
    val theme: AppTheme = AppTheme.System
)
