package ru.mobileup.samples.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun Dp.toPx(): Int {
    return with(LocalDensity.current) { roundToPx() }
}

@Composable
fun Float.toDp(): Dp {
    return with(LocalDensity.current) { toDp() }
}

@Composable
fun Int.toDp(): Dp {
    return with(LocalDensity.current) { toDp() }
}

fun Modifier.clickableNoRipple(enabled: Boolean = true, listener: () -> Unit) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = listener,
        enabled = enabled
    )
}

fun Modifier.zoomable(
    minScale: Float,
    maxScale: Float,
    onEndOfContentReached: (Boolean) -> Unit
): Modifier = composed {
    var scale by remember { mutableFloatStateOf(minScale) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    this.pointerInput(Unit) {
        awaitEachGesture {
            do {
                val event = awaitPointerEvent()

                val zoom = event.calculateZoom()
                val pan = event.calculatePan()

                val maxX = (size.width * (scale - 1)) / 2
                val minX = -maxX
                val offsetX = maxOf(minX, minOf(maxX, offset.x + pan.x))
                val maxY = (size.height * (scale - 1)) / 2
                val minY = -maxY
                val offsetY = maxOf(minY, minOf(maxY, offset.y + pan.y))

                offset = offset.copy(x = offsetX, y = offsetY)
                scale = maxOf(minScale, minOf(scale * zoom, maxScale))

                val imageWidth = size.width * scale
                val distanceToBorderPx =
                    imageWidth - size.width - 2 * abs(offset.x)
                onEndOfContentReached(scale <= 1 || distanceToBorderPx <= 0)
            } while (event.changes.any { it.pressed })
        }
    }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
            translationX = offset.x,
            translationY = offset.y,
        )
}

@Composable
fun InteractionSource.collectIsLongPressedAsState(): State<Boolean> {
    val onPress = remember { mutableStateOf(false) }
    LaunchedEffect(this) {
        var job: Job? = null
        interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    job = launch {
                        while (true) {
                            delay(250)
                            onPress.value = true
                        }
                    }
                }
                is PressInteraction.Release,
                is PressInteraction.Cancel -> job?.cancel()
            }
            // reset state with some delay otherwise it can re-emit the previous state
            delay(10L)
            onPress.value = false
        }
    }
    return onPress
}