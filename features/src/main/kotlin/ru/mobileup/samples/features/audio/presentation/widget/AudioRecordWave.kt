package ru.mobileup.samples.features.audio.presentation.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.flow.collectLatest
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.audio.domain.recorder.AudioAmplitude
import ru.mobileup.samples.features.audio.domain.wave.AudioAmplitudeGenerator

private const val AMPLITUDES_COUNT = 20
private const val MIN_AMPLITUDE_HEIGHT = 6f
private const val MAX_AMPLITUDE_HEIGHT = 50f
private const val FADING_ANIMATION_DURATION = 1500

@Composable
fun AudioRecordWave(
    amplitudeHolder: AudioAmplitudeGenerator,
    modifier: Modifier = Modifier,
) {

    var amplitudesValues by remember {
        mutableStateOf(
            List(AMPLITUDES_COUNT) { AudioAmplitude.zero }
        )
    }

    LaunchedEffect(amplitudeHolder) {
        amplitudeHolder.amplitudesFlow.collectLatest {
            val currentValues = amplitudesValues.toMutableList()
            currentValues.removeAt(0)
            currentValues.add(amplitudesValues.lastIndex, it)
            amplitudesValues = currentValues
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = MAX_AMPLITUDE_HEIGHT.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        amplitudesValues.forEach { audioAmplitude ->
            key(audioAmplitude.id) {

                val animatedHeight = remember { Animatable(MIN_AMPLITUDE_HEIGHT) }

                LaunchedEffect(Unit) {
                    animatedHeight.animateTo(
                        targetValue = lerp(MIN_AMPLITUDE_HEIGHT, MAX_AMPLITUDE_HEIGHT, audioAmplitude.value),
                        animationSpec = spring(DampingRatioHighBouncy)
                    )
                    animatedHeight.animateTo(
                        targetValue = MIN_AMPLITUDE_HEIGHT,
                        animationSpec = tween(durationMillis = FADING_ANIMATION_DURATION)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(MIN_AMPLITUDE_HEIGHT.dp)
                        .clip(CircleShape)
                        .background(CustomTheme.colors.button.primary)
                        .layout { measurable, constraints ->
                            val heightPx = animatedHeight.value.dp.roundToPx()
                            val placeable = measurable.measure(
                                constraints.copy(
                                    minHeight = heightPx,
                                    maxHeight = heightPx
                                )
                            )
                            layout(placeable.width, placeable.height) {
                                placeable.place(0, 0)
                            }
                        }
                )
            }
        }
    }
}