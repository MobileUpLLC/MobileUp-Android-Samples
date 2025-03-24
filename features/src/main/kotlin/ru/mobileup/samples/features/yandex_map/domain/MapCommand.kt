package ru.mobileup.samples.features.yandex_map.domain

import android.util.Log
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.mapview.MapView
import ru.mobileup.samples.core.location.GeoCoordinate
import ru.mobileup.samples.features.yandex_map.utils.changeZoomByStep
import ru.mobileup.samples.features.yandex_map.utils.moveTo
import ru.mobileup.samples.features.yandex_map.utils.moveToBoundingBox
import kotlin.math.max

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

    data class MapHeightRect(
        val heightShrink: Float
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

        is MapCommand.MapHeightRect -> {
            try {
                mapWindow.focusRect = ScreenRect(
                    ScreenPoint(0f, 0f),
                    ScreenPoint(
                        max(1.0f, mapWindow.width().toFloat()),
                        max(1.0f, mapWindow.height().toFloat() - command.heightShrink)
                    )
                )
            } catch (e: Exception) {
                Log.d("YandexMap", "MapCommand.MapHeightRect error ${e.message}")
            }
        }
    }
}