package ru.mobileup.samples.features.video.presentation.recorder

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.data.render.availableFilters
import ru.mobileup.samples.features.video.data.utils.name
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.events.CameraEvent
import ru.mobileup.samples.features.video.domain.events.RecordingResult
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.presentation.recorder.controller.CameraController
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderCameraSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderFilterSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderFpsSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderQualitySelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderTorchSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecordingButton

@Composable
fun VideoRecorderUi(
    component: VideoRecorderComponent,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    val recorderConfig by component.recorderConfig.collectAsState()
    val recorderState by component.recorderState.collectAsState()

    val previewView by remember(context, lifecycleOwner) {
        mutableStateOf(
            PreviewView(context).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        )
    }

    val filtersPagerState = rememberPagerState(
        initialPage = 0,
    ) { availableFilters.size }

    val cameraController by remember(context, lifecycleOwner, previewView) {
        mutableStateOf(
            CameraController(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                onCameraRecordingEvent = {
                    when (it) {
                        CameraEvent.StartRecord -> {
                            component.onUpdateIsRecording(true)
                            component.onUpdateConfig(RecorderConfig.Off)
                        }

                        is CameraEvent.ProgressRecord -> {
                            component.onUpdateDuration(it.recordDuration)
                        }

                        is CameraEvent.StopRecord -> {
                            component.onUpdateIsRecording(false)

                            when (val result = it.recordingResult) {
                                is RecordingResult.Success -> {
                                    component.onRecordCompleted(result.uri)
                                }

                                is RecordingResult.Error -> {
                                    component.onRecordFailed()
                                }
                            }
                        }
                    }
                },
                onCameraInitializationFailed = {
                    component.onRecordInitializationFailed()
                },
            )
        )
    }

    val previewTransformableState = rememberTransformableState { zoomChange, _, _ ->
        cameraController.zoomChange(zoomChange = zoomChange)
    }

    SystemBars(transparentNavigationBar = true)

    LaunchedEffect(filtersPagerState.settledPage, filtersPagerState.isScrollInProgress) {
        val newEffectIndex = filtersPagerState.settledPage % availableFilters.size
        if (cameraController.glFilter.ordinal != newEffectIndex && !filtersPagerState.isScrollInProgress) {
            val filter = availableFilters[newEffectIndex]
            cameraController.glFilter = filter
        }
    }

    LaunchedEffect(recorderState) {
        cameraController.recorderState = recorderState
    }

    DisposableEffect(cameraController) {
        onDispose {
            cameraController.release()
        }
    }

    VideoRecorderContent(
        modifier = modifier,
        component = component,
        recorderConfig = recorderConfig,
        recorderState = recorderState,
        filtersPagerState = filtersPagerState,
        previewTransformableState = previewTransformableState,
        previewView = previewView,
        cameraController = cameraController,
        onUpdateConfig = {
            if (!recorderState.isRecording) {
                component.onUpdateConfig(
                    if (recorderConfig == it) {
                        RecorderConfig.Off
                    } else {
                        it
                    }
                )
            }
        }
    )
}

@Composable
private fun VideoRecorderContent(
    component: VideoRecorderComponent,
    recorderConfig: RecorderConfig,
    recorderState: RecorderState,
    filtersPagerState: PagerState,
    previewTransformableState: TransformableState,
    previewView: PreviewView,
    cameraController: CameraController,
    onUpdateConfig: (RecorderConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val recorderDurationString by remember(recorderState.durationMs) {
        derivedStateOf { formatMillisToMS(recorderState.durationMs) }
    }

    val fling = PagerDefaults.flingBehavior(
        state = filtersPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(15)
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CustomTheme.colors.palette.black)
                    .padding(horizontal = 8.dp, vertical = 24.dp)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Recorder",
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                )

                Text(
                    text = recorderState.cameraSelector.name(),
                    color = if (recorderConfig == RecorderConfig.Camera) {
                        CustomTheme.colors.text.warning
                    } else {
                        CustomTheme.colors.text.invert
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.Camera)
                        }
                )

                Text(
                    text = recorderState.fps.toString(),
                    color = if (recorderConfig == RecorderConfig.FPS) {
                        CustomTheme.colors.text.warning
                    } else {
                        CustomTheme.colors.text.invert
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.FPS)
                        }
                )

                Text(
                    text = recorderState.quality.name(),
                    color = if (recorderConfig == RecorderConfig.Quality) {
                        CustomTheme.colors.text.warning
                    } else {
                        CustomTheme.colors.text.invert
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.Quality)
                        }
                )

                Icon(
                    painter = painterResource(
                        id = if (recorderState.torchState) {
                            R.drawable.ic_torch
                        } else {
                            R.drawable.ic_torch_disabled
                        }
                    ),
                    contentDescription = "torch",
                    tint = if (recorderConfig == RecorderConfig.Torch) {
                        CustomTheme.colors.icon.warning
                    } else {
                        Color.Unspecified
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.Torch)
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "filter",
                    tint = if (recorderConfig == RecorderConfig.Filter) {
                        CustomTheme.colors.icon.warning
                    } else {
                        Color.Unspecified
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.Filter)
                        }
                )
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(previewTransformableState),
                factory = remember { { previewView } },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Box {
                RecorderCameraSelector(
                    recorderConfig = recorderConfig,
                    cameraSelector = recorderState.cameraSelector,
                    onCameraSelect = component::onUpdateCameraSelector
                )

                RecorderFpsSelector(
                    recorderConfig = recorderConfig,
                    fps = recorderState.fps,
                    onFpsSelect = component::onUpdateFps
                )

                RecorderQualitySelector(
                    recorderConfig = recorderConfig,
                    quality = recorderState.quality,
                    onQualitySelect = component::onUpdateQuality
                )

                RecorderTorchSelector(
                    recorderConfig = recorderConfig,
                    torchState = recorderState.torchState,
                    onTorchSelect = {
                        component.onUpdateTorchState(it)
                        cameraController.changeTorchState(it)
                    }
                )

                RecorderFilterSelector(
                    recorderConfig = recorderConfig,
                    filtersPagerState = filtersPagerState,
                    fling = fling
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CustomTheme.colors.palette.black.copy(alpha = 0.3f))
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                RecordingButton(
                    isRecording = recorderState.isRecording,
                    onClick = {
                        if (recorderState.isRecording) {
                            if (recorderState.durationMs < 1000) {
                                component.onRecordStopFailed()
                            } else {
                                cameraController.stopRecording()
                            }
                        } else {
                            cameraController.startRecording()
                        }
                    }
                )

                Text(
                    text = if (recorderState.isRecording) {
                        recorderDurationString
                    } else {
                        ""
                    },
                    color = CustomTheme.colors.text.invert,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun VideoRecorderUiPreview() {
    AppTheme {
        VideoRecorderUi(FakeVideoRecorderComponent())
    }
}