package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.RecorderConfig

@Composable
fun BoxScope.CameraEffectIcon(
    recorderConfig: RecorderConfig,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filter),
        contentDescription = "filter",
        tint = if (recorderConfig == RecorderConfig.Filter) {
            CustomTheme.colors.icon.warning
        } else {
            Color.Unspecified
        },
        modifier = Modifier
            .padding(end = 16.dp)
            .size(32.dp)
            .align(Alignment.CenterEnd)
            .clickableNoRipple {
                onClick()
            }
    )
}

@Preview
@Composable
private fun CameraEffectconPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box {
                CameraEffectIcon(
                    recorderConfig = RecorderConfig.Off,
                    onClick = { }
                )
            }

            Box {
                CameraEffectIcon(
                    recorderConfig = RecorderConfig.Filter,
                    onClick = { }
                )
            }
        }
    }
}