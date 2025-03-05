package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation
import kotlin.math.abs
import kotlin.math.roundToInt

private val SPEED_VALUES = floatArrayOf(
    0.5f, 1f, 1.5f, 2f, 3f, 4f, 5f
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.PlayerSpeedSelector(
    playerConfig: PlayerConfig,
    speed: Float,
    onUpdateSpeed: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = playerConfig == PlayerConfig.Speed,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(horizontal = 8.dp)
        ) {
            Slider(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                steps = SPEED_VALUES.size - 2,
                track = { sliderState ->
                    SliderDefaults.Track(
                        modifier = Modifier.height(4.dp),
                        sliderState = sliderState,
                        thumbTrackGapSize = 0.dp,
                        colors = SliderDefaults.colors().copy(
                            activeTrackColor = CustomTheme.colors.palette.white,
                            inactiveTrackColor = Color.White.copy(0.7f),
                            activeTickColor = CustomTheme.colors.palette.white,
                            inactiveTickColor = CustomTheme.colors.palette.white
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
                value = speed.speedToProgress(),
                onValueChange = {
                    onUpdateSpeed(it.progressToSpeed())
                }
            )

            Text(
                text = "${speed}X",
                color = if (speed == 1f) {
                    CustomTheme.colors.text.invert
                } else {
                    CustomTheme.colors.text.warning
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            )
        }
    }
}

private fun Float.progressToSpeed(): Float {
    val index = (this * SPEED_VALUES.lastIndex).roundToInt().coerceIn(0, SPEED_VALUES.lastIndex)
    return SPEED_VALUES[index]
}

private fun Float.speedToProgress(): Float {
    val index = SPEED_VALUES.withIndex()
        .minBy { (_, value) -> abs(value - this) }.index // Nearest value index
    return index.toFloat() / SPEED_VALUES.lastIndex
}

@Preview
@Composable
private fun PlayerSpeedSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            PlayerSpeedSelector(
                playerConfig = PlayerConfig.Speed,
                speed = 1f,
                onUpdateSpeed = { }
            )
        }
    }
}