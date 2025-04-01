package ru.mobileup.samples.core.map.presentation

import com.yandex.mapkit.mapview.MapView

interface MapOverlay {
    fun setup(mapView: MapView)
}