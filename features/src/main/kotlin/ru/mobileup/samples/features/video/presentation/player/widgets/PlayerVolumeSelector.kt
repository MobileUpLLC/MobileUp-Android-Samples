package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.PlayerVolumeSelector(
    playerConfig: PlayerConfig,
    volume: Float,
    onUpdateVolume: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = playerConfig == PlayerConfig.Volume,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sound_off),
                contentDescription = "volume",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )

            Slider(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
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
                value = volume,
                onValueChange = {
                    onUpdateVolume(it)
                }
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_sound_on),
                contentDescription = "volume",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerVolumeSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            PlayerVolumeSelector(
                playerConfig = PlayerConfig.Volume,
                volume = 1f,
                onUpdateVolume = { }
            )
        }
    }
}