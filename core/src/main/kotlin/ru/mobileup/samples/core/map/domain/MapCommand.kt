package ru.mobileup.samples.core.map.domain

import com.yandex.mapkit.mapview.MapView
import ru.mobileup.samples.core.location.GeoCoordinate
import ru.mobileup.samples.core.map.utils.changeZoomByStep
import ru.mobileup.samples.core.map.utils.moveTo
import ru.mobileup.samples.core.map.utils.moveToBoundingBox

sealed interface MapCommand {
    data class MoveTo(
        val coordinate: GeoCoordinate,
        val zoom: Float? = null, // null means don't change
        val animate: Boolean = true
    ) : MapCommand

    data object ZoomIn : MapCommand

    data object ZoomOut : MapCommand

    data class MoveToBoundingBox(
        val coordinates: List<GeoCoordinate>,
        val zoomShrink: Float = 0.3f,
        val animate: Boolean = false
    ) : MapCommand
}

fun MapView.executeCommand(command: MapCommand) {
    when (command) {
        is MapCommand.MoveTo -> moveTo(
            coordinate = command.coordinate,
            zoom = command.zoom,
            animate = command.animate
        )

        is MapCommand.ZoomIn -> {
            changeZoomByStep(1.0f, animate = true)
        }

        is MapCommand.ZoomOut -> {
            changeZoomByStep(-1.0f, animate = true)
        }

        is MapCommand.MoveToBoundingBox -> {
            moveToBoundingBox(
                coordinates = command.coordinates,
                zoomShrink = command.zoomShrink,
                animate = command.animate
            )
        }
    }
}