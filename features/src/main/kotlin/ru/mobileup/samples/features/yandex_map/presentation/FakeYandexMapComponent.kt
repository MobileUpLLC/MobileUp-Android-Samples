package ru.mobileup.samples.features.yandex_map.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.fakeSimpleDialogControl
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.dialog.standard.fakeStandardDialogControl
import ru.mobileup.samples.core.location.GeoCoordinate
import ru.mobileup.samples.core.utils.LoadableState
import ru.mobileup.samples.features.yandex_map.domain.MapCommand
import ru.mobileup.samples.features.yandex_map.domain.YandexMapTheme

class FakeYandexMapComponent : YandexMapComponent {
    override val mapCommands: Flow<MapCommand> = emptyFlow()
    override val isCurrentLocationAvailable = MutableStateFlow(true)
    override val isLocationSearchInProgress = MutableStateFlow(true)
    override val theme = MutableStateFlow(YandexMapTheme.Default)
    override val placesState = MutableStateFlow(LoadableState<List<GeoCoordinate>>(data = emptyList()))
    override val placeDialogControl: SimpleDialogControl<GeoCoordinate> =
        fakeSimpleDialogControl(data = GeoCoordinate.KREMLIN)
    override val locationDialogControl: StandardDialogControl = fakeStandardDialogControl()

    override fun onMyLocationClick() = Unit
    override fun onZoomInClick() = Unit
    override fun onZoomOutClick() = Unit
    override fun onPlaceClick(place: GeoCoordinate) = Unit
    override fun onThemeSwitch(newTheme: YandexMapTheme) = Unit
    override fun onRetryClick() = Unit
}