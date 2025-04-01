package ru.mobileup.samples.features.yandex_map.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.location.GeoCoordinate
import ru.mobileup.samples.core.map.domain.MapCommand
import ru.mobileup.samples.core.map.domain.MapTheme
import ru.mobileup.samples.core.utils.LoadableState

interface YandexMapComponent {
    val mapCommands: Flow<MapCommand>
    val isCurrentLocationAvailable: StateFlow<Boolean>
    val isLocationSearchInProgress: StateFlow<Boolean>
    val theme: StateFlow<MapTheme>
    val placesState: StateFlow<LoadableState<List<GeoCoordinate>>>
    val placeDialogControl: SimpleDialogControl<GeoCoordinate>
    val locationDialogControl: StandardDialogControl

    fun onZoomInClick()
    fun onZoomOutClick()
    fun onMyLocationClick()
    fun onPlaceClick(place: GeoCoordinate)
    fun onThemeSwitch(newTheme: MapTheme)
    fun onRetryClick()
}
