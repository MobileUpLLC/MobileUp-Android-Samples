package ru.mobileup.samples.features.yandex_map.presentation.widget

import com.yandex.mapkit.mapview.MapView

interface MapOverlay {
    fun setup(mapView: MapView)
}