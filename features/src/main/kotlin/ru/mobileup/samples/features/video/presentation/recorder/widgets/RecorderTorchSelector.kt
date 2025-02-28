package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@Composable
fun BoxScope.RecorderTorchSelector(
    recorderConfig: RecorderConfig,
    torchState: Boolean,
    onTorchSelected: (Boolean) -> Unit
) {
    SlideAnimation(
        modifier = Modifier.align(Alignment.BottomStart),
        isVisible = recorderConfig == RecorderConfig.Torch
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
        ) {
            RecorderState.availableTorchStateList.forEach {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onTorchSelected(it)
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(
                            if (it) {
                                R.string.recorder_enable
                            } else {
                                R.string.recorder_disable
                            }
                        ),
                        color = if (it == torchState) {
                            CustomTheme.colors.text.warning
                        } else {
                            CustomTheme.colors.text.invert
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Icon(
                        painter = painterResource(
                            id = if (it) {
                                R.drawable.ic_torch
                            } else {
                                R.drawable.ic_torch_disabled
                            }
                        ),
                        contentDescription = "torch",
                        tint = if (it == torchState) {
                            CustomTheme.colors.icon.warning
                        } else {
                            Color.Unspecified
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecorderTorchSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            RecorderTorchSelector(
                recorderConfig = RecorderConfig.FPS,
                torchState = false,
                onTorchSelected = { }
            )
        }
    }
}