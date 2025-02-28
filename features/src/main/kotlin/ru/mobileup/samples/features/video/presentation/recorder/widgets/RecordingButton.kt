package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme

private const val ANIMATION_DURATION_MS = 500
private const val BUTTON_SIZE_DP = 64

@Composable
fun RecordingButton(
    isRecording: Boolean,
    onClick: () -> Unit,
) {
    val animationDuration by remember { mutableIntStateOf(ANIMATION_DURATION_MS) }

    val outerCircleColor by animateColorAsState(
        targetValue = if (isRecording) {
            CustomTheme.colors.palette.white50
        } else {
            CustomTheme.colors.palette.white
        },
        animationSpec = tween(animationDuration),
        label = "outerCircleColor"
    )

    val innerCircleColor by animateColorAsState(
        targetValue = if (isRecording) {
            CustomTheme.colors.palette.white10
        } else {
            CustomTheme.colors.palette.white
        },
        animationSpec = tween(animationDuration),
        label = "innerCircleColor"
    )

    val stopIconColor by animateColorAsState(
        targetValue = if (isRecording) {
            CustomTheme.colors.icon.error
        } else {
            Color.Transparent
        },
        animationSpec = tween(animationDuration),
        label = "stopIconColor"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (isRecording) 4.dp else 200.dp,
        animationSpec = tween(animationDuration),
        label = "cornerRadius"
    )

    CircleButton(
        modifier = Modifier
            .size(BUTTON_SIZE_DP.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        outerCircleColor = outerCircleColor,
        innerCircleColor = innerCircleColor,
        stopIconColor = stopIconColor,
        cornerRadius = cornerRadius
    )
}

@Composable
private fun CircleButton(
    modifier: Modifier = Modifier,
    outerCircleColor: Color,
    innerCircleColor: Color,
    stopIconColor: Color,
    cornerRadius: Dp
) {
    Canvas(modifier = modifier) {
        drawCircle(
            brush = SolidColor(outerCircleColor),
            style = Stroke(
                width = 8.dp.toPx()
            )
        )

        drawCircle(
            radius = size.maxDimension / 2.5f,
            brush = SolidColor(innerCircleColor),
        )

        drawRoundRect(
            brush = SolidColor(stopIconColor),
            topLeft = Offset(size.maxDimension / 3, size.maxDimension / 3),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            size = size / 3f
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