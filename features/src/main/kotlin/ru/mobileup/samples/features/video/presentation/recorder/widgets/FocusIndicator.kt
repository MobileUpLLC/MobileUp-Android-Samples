package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.toPx
import ru.mobileup.samples.features.video.presentation.player.widgets.CustomThumb

private const val INDICATOR_SIZE_DP = 44
private const val SLIDER_WIDTH_DP = 100
private const val SLIDER_HEIGHT_DP = 16

private const val INDICATOR_ANIMATION_DURATION_MS = 200
private const val INDICATOR_VISIBILITY_DURATION_MS = 2000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusIndicator(
    offset: Offset,
    onExposureChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val widthPx = SLIDER_WIDTH_DP.dp.toPx()
    val heightPx = ((INDICATOR_SIZE_DP + SLIDER_HEIGHT_DP).dp + 8.dp).toPx()

    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val indicatorScale = remember { Animatable(1f) }

    var exposureValue by remember {
        mutableFloatStateOf(0f)
    }

    val intOffset by remember(offset) {
        derivedStateOf {
            IntOffset(
                x = (offset.x - widthPx / 2).toInt(),
                y = (offset.y - heightPx / 2).toInt()
            )
        }
    }

    var isVisible by remember {
        mutableStateOf(false)
    }

    var hideJob by remember {
        mutableStateOf<Job?>(null)
    }

    fun hide() {
        hideJob?.cancel()
        hideJob = scope.launch {
            delay(INDICATOR_VISIBILITY_DURATION_MS)
            isVisible = false
        }
    }

    LaunchedEffect(offset) {
        if (offset.x == -1f && offset.y == -1f) {
            return@LaunchedEffect
        }

        isVisible = true

        indicatorScale.snapTo(0.9f)

        indicatorScale.animateTo(
            1.2f,
            animationSpec = tween(
                durationMillis = INDICATOR_ANIMATION_DURATION_MS,
                easing = LinearEasing
            )
        )

        indicatorScale.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = INDICATOR_ANIMATION_DURATION_MS,
                easing = LinearEasing
            )
        )
        hide()
    }

    if (isVisible) {
        Column(
            modifier = modifier.offset {
                intOffset
            }
        ) {
            Canvas(
                modifier = Modifier
                    .size(INDICATOR_SIZE_DP.dp)
                    .align(Alignment.CenterHorizontally)
                    .scale(indicatorScale.value)
            ) {
                drawCircle(
                    brush = SolidColor(Color.White),
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )
            }

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Slider(
                    modifier = Modifier
                        .width(SLIDER_WIDTH_DP.dp)
                        .padding(top = 8.dp),
                    track = { sliderState ->
                        SliderDefaults.Track(
                            modifier = Modifier.height(4.dp),
                            sliderState = sliderState,
                            thumbTrackGapSize = 0.dp,
                            colors = SliderDefaults.colors().copy(
                                activeTrackColor = CustomTheme.colors.palette.white,
                                inactiveTrackColor = Color.White.copy(0.7f),
                            )
                        )
                    },
                    interactionSource = interactionSource,
                    thumb = {
                        CustomThumb(
                            interactionSource = interactionSource,
                            colors = SliderDefaults.colors(
                                thumbColor = CustomTheme.colors.palette.white
                            ),
                            thumbSize = DpSize(16.dp, 16.dp),
                        )
                    },
                    value = exposureValue,
                    valueRange = -1f..1f,
                    onValueChange = {
                        exposureValue = it
                        onExposureChange(it)
                        hide()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FocusIndicatorPreview() {
    AppTheme {
        Box(modifier = Modifier.size(300.dp)) {
            FocusIndicator(
                offset = Offset(
                    x = 150.dp.toPx().toFloat(),
                    y = 150.dp.toPx().toFloat()
                ),
                onExposureChange = { }
            )
        }
    }
}