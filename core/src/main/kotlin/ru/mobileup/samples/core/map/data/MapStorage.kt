package ru.mobileup.samples.core.map.data

import ru.mobileup.samples.core.map.domain.MapTheme

interface MapStorage {
    suspend fun getTheme(): MapTheme
    suspend fun setTheme(mapTheme: MapTheme)
}