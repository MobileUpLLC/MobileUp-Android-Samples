package ru.mobileup.samples.core.map.utils

import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import ru.mobileup.samples.core.location.GeoCoordinate

private const val ANIMATION_DURATION_SECONDS = 1.0f

fun MapView.moveTo(
    coordinate: GeoCoordinate,
    zoom: Float? = null,
    azimuth: Float? = null,
    tilt: Float? = null,
    animate: Boolean
) {
    val currentPosition = mapWindow.map.cameraPosition

    val position = CameraPosition(
        coordinate.toPoint(),
        zoom ?: currentPosition.zoom,
        azimuth ?: currentPosition.azimuth,
        tilt ?: currentPosition.tilt
    )

    mapWindow.map.move(position, animate)
}

fun MapView.changeZoomByStep(value: Float, animate: Boolean) {
    val currentPosition = mapWindow.map.cameraPosition

    val position = CameraPosition(
        currentPosition.target,
        currentPosition.zoom + value,
        currentPosition.azimuth,
        currentPosition.tilt
    )

    mapWindow.map.move(position, animate)
}

fun MapView.moveToBoundingBox(
    coordinates: List<GeoCoordinate>,
    zoomShrink: Float,
    azimuth: Float? = null,
    tilt: Float? = null,
    animate: Boolean
) {
    if (coordinates.isEmpty()) return
    val points = coordinates.map { it.toPoint() }
    val geometry = Geometry.fromPolyline(Polyline(points))
    val currentPosition = mapWindow.map.cameraPosition

    val position = mapWindow.map.cameraPosition(
        geometry,
        azimuth ?: currentPosition.azimuth,
        tilt ?: currentPosition.tilt,
        null
    )

    val shrunkPosition = CameraPosition(
        position.target,
        position.zoom - zoomShrink,
        position.azimuth,
        position.tilt
    )

    mapWindow.map.move(shrunkPosition, animate)
}

private fun Map.move(position: CameraPosition, animate: Boolean) {
    if (animate) {
        move(position, Animation(Animation.Type.SMOOTH, ANIMATION_DURATION_SECONDS), null)
    } else {
        move(position)
    }
}

fun GeoCoordinate.toPoint(): Point {
    return Point(lat, lng)
}

fun Point.toGeoCoordinate(): GeoCoordinate {
    return GeoCoordinate(latitude, longitude)
}