package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@Composable
fun BoxScope.RecorderFpsSelector(
    recorderConfig: RecorderConfig,
    fps: Int,
    onFpsSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = recorderConfig == RecorderConfig.FPS
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
        ) {
            RecorderState.availableFpsList.forEach {
                RecorderSelectorText(
                    text = it.toString(),
                    isActive = it == fps,
                    onClick = {
                        onFpsSelect(it)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecorderFpsSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            RecorderFpsSelector(
                recorderConfig = RecorderConfig.FPS,
                fps = 60,
                onFpsSelect = { }
            )
        }
    }
}