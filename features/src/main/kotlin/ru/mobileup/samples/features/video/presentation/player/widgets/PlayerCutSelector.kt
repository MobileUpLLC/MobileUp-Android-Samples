package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.PlayerCutSelector(
    playerConfig: PlayerConfig,
    startPositionMs: Long,
    endPositionMs: Long,
    maxDurationMs: Long,
    speed: Float,
    onCut: (Long, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }

    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = playerConfig == PlayerConfig.Cut
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.player_cut_start),
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = formatMillisToMS((startPositionMs / speed).toLong()),
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier
                )
            }

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 36.dp) {
                RangeSlider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    startInteractionSource = startInteractionSource,
                    endInteractionSource = endInteractionSource,
                    track = { sliderState ->
                        SliderDefaults.Track(
                            modifier = Modifier.height(4.dp),
                            rangeSliderState = sliderState,
                            thumbTrackGapSize = 0.dp,
                            colors = SliderDefaults.colors().copy(
                                activeTrackColor = CustomTheme.colors.palette.white,
                                inactiveTrackColor = Color.White.copy(0.7f),
                            )
                        )
                    },
                    startThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = startInteractionSource,
                            colors = SliderDefaults.colors().copy(
                                thumbColor = CustomTheme.colors.palette.white
                            ),
                            enabled = true,
                            thumbSize = DpSize(4.dp, 16.dp)
                        )
                    },
                    endThumb = {
                        SliderDefaults.Thumb(
                            interactionSource = endInteractionSource,
                            colors = SliderDefaults.colors().copy(
                                thumbColor = CustomTheme.colors.palette.white
                            ),
                            enabled = true,
                            thumbSize = DpSize(4.dp, 16.dp)
                        )
                    },
                    value = startPositionMs.toFloat()..endPositionMs.toFloat(),
                    valueRange = 0f..maxDurationMs.toFloat(),
                    onValueChange = { range ->
                        onCut(range.start.toLong(), range.endInclusive.toLong())
                    }
                )
            }

            Column {
                Text(
                    text = stringResource(R.string.player_cut_end),
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = formatMillisToMS((endPositionMs / speed).toLong()),
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlayerCutSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            PlayerCutSelector(
                playerConfig = PlayerConfig.Cut,
                startPositionMs = 0,
                endPositionMs = 10_000,
                maxDurationMs = 10_000,
                speed = 1f,
                onCut = { _, _ -> }
            )
        }
    }
}