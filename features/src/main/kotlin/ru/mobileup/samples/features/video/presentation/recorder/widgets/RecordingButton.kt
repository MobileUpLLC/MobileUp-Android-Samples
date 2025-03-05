package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme

private const val ANIMATION_DURATION_MS = 500

@Composable
fun ColumnScope.RecordingButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animationDuration by remember { mutableIntStateOf(ANIMATION_DURATION_MS) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isRecording) {
            CustomTheme.colors.palette.white10
        } else {
            CustomTheme.colors.palette.white
        },
        animationSpec = tween(animationDuration),
        label = "gradientIndicatorColor"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isRecording) {
            CustomTheme.colors.icon.error
        } else {
            Color.Transparent
        },
        animationSpec = tween(animationDuration),
        label = "color"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isRecording) 4.dp else 200.dp,
        animationSpec = tween(animationDuration),
        label = "radius"
    )

    Box(
        modifier = modifier
            .size(64.dp)
            .align(Alignment.CenterHorizontally)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable {
                onClick()
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(20.dp)
                .clip(RoundedCornerShape(corner = CornerSize(cornerRadius)))
                .background(iconColor)
        )
    }
}

@Preview
@Composable
private fun RecordingButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            RecordingButton(
                isRecording = false,
                onClick = { }
            )

            RecordingButton(
                isRecording = true,
                onClick = { }
            )
        }
    }
}