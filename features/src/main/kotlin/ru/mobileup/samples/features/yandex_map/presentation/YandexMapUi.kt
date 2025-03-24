package ru.mobileup.samples.features.yandex_map.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.BottomSheet
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.features.yandex_map.presentation.widget.MyLocationMarkerOverlay
import ru.mobileup.samples.features.yandex_map.presentation.widget.PlacePinsOverlay
import ru.mobileup.samples.features.yandex_map.presentation.widget.YandexMapMyLocationButton
import ru.mobileup.samples.features.yandex_map.presentation.widget.YandexMapThemeSwitch
import ru.mobileup.samples.features.yandex_map.presentation.widget.YandexMapViewUi
import ru.mobileup.samples.features.yandex_map.presentation.widget.YandexMapZoomButtons

@Composable
fun YandexMapUi(
    component: YandexMapComponent,
    modifier: Modifier = Modifier
) {
    SystemBars(transparentNavigationBar = true)

    val isCurrentLocationAvailable by component.isCurrentLocationAvailable.collectAsState()
    val isLocationSearchInProgress by component.isLocationSearchInProgress.collectAsState()
    val placesState by component.placesState.collectAsState()
    val theme by component.theme.collectAsState()

    val myLocationOverlay = remember { MyLocationMarkerOverlay() }
    val placesPinOverlay = remember { PlacePinsOverlay(component::onPlaceClick) }

    LaunchedEffect(isCurrentLocationAvailable) {
        myLocationOverlay.updateIsLocationEnable(isCurrentLocationAvailable)
    }

    LaunchedEffect(placesState) {
        placesState.data?.let(placesPinOverlay::updatePlaces)
    }

    Box(
        modifier = modifier,
    ) {
        YandexMapViewUi(
            mapCommands = component.mapCommands,
            theme = theme,
            overlays = listOf(myLocationOverlay, placesPinOverlay)
        )

        YandexMapZoomButtons(
            modifier = Modifier
                .align(BiasAlignment(0.9f, 0f)),
            onZoomInClick = component::onZoomInClick,
            onZoomOutClick = component::onZoomOutClick
        )
        YandexMapMyLocationButton(
            modifier = Modifier
                .align(BiasAlignment(0.9f, 0.9f)),
            onClick = component::onMyLocationClick,
            isEnabled = !isLocationSearchInProgress
        )

        YandexMapThemeSwitch(
            theme = theme,
            onThemeSwitch = component::onThemeSwitch,
            modifier = Modifier
                .align(BiasAlignment(-0.9f, 0f)),
        )

        LoaderOverlay(placesState.loading)
    }

    BottomSheet(component.placeDialogControl) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Lat = ${it.lat}",
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Text(
                text = "Lng = ${it.lng}",
            )
        }
    }

    StandardDialog(dialogControl = component.locationDialogControl)
}

@Composable
private fun LoaderOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.7f))
                .clickableNoRipple {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Black
            )
        }
    }
}