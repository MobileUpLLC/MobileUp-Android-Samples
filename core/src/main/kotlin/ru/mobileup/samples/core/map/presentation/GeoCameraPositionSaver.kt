package ru.mobileup.samples.core.map.presentation

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import ru.mobileup.samples.core.location.DEFAULT_MAP_ZOOM
import ru.mobileup.samples.core.location.GeoCameraPosition
import ru.mobileup.samples.core.location.GeoCoordinate

val GeoCameraPositionSaver: Saver<GeoCameraPosition, *> = listSaver(
    save = { position ->
        listOf(
            position.geoCoordinate.lat,
            position.geoCoordinate.lng,
            position.zoom,
            position.azimuth,
            position.tilt
        )
    },
    restore = { values ->
        GeoCameraPosition(
            geoCoordinate = GeoCoordinate(
                lat = values.getSafe(0, GeoCoordinate.Companion.KREMLIN.lat),
                lng = values.getSafe(1, GeoCoordinate.Companion.KREMLIN.lng)
            ),
            zoom = values.getSafe(2, DEFAULT_MAP_ZOOM),
            azimuth = 0f,
            tilt = 0f
        )
    }
)

private inline fun <reified T> List<*>.getSafe(index: Int, default: T): T {
    return try {
        this.getOrNull(index) as? T ?: default
    } catch (_: ClassCastException) {
        default
    }
}