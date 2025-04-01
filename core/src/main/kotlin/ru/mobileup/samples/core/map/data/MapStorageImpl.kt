package ru.mobileup.samples.core.map.data

import ru.mobileup.samples.core.settings.SettingsFactory
import ru.mobileup.samples.core.map.domain.MapTheme
import ru.mobileup.samples.core.map.domain.MapTheme.Bright
import ru.mobileup.samples.core.map.domain.MapTheme.Dark
import ru.mobileup.samples.core.map.domain.MapTheme.Default

class MapStorageImpl(
    settingsFactory: SettingsFactory
) : MapStorage {

    private companion object {
        const val KEY_MAP_THEME = "map_theme_key"
        const val MAP_SETTINGS_NAME = "map_settings_name"
    }

    private val settings = settingsFactory.createSettings(MAP_SETTINGS_NAME)

    override suspend fun getTheme() = settings.getString(KEY_MAP_THEME).toMapTheme()

    override suspend fun setTheme(mapTheme: MapTheme) {
        settings.putString(KEY_MAP_THEME, mapTheme.name)
    }
}

private fun String?.toMapTheme(): MapTheme = when (this) {
    Bright.name -> Bright
    Dark.name -> Dark
    else -> Default
}
