package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.camera.core.CameraSelector
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
import ru.mobileup.samples.features.video.data.utils.name
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@Composable
fun BoxScope.RecorderCameraSelector(
    recorderConfig: RecorderConfig,
    cameraSelector: CameraSelector,
    onCameraSelect: (CameraSelector) -> Unit,
    modifier: Modifier = Modifier
) {
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = recorderConfig == RecorderConfig.Camera
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
        ) {
            RecorderState.availableCameraSelectorList.forEach {
                RecorderSelectorText(
                    text = it.name(),
                    isActive = it == cameraSelector,
                    onClick = {
                        onCameraSelect(it)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecorderCameraSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            RecorderCameraSelector(
                recorderConfig = RecorderConfig.Camera,
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
                onCameraSelect = { }
            )
        }
    }
}