package ru.mobileup.samples.features.video.presentation.recorder

import android.graphics.Bitmap
import androidx.camera.core.FocusMeteringAction
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.message.presentation.noOverlapByMessage
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.data.render.availableFilters
import ru.mobileup.samples.features.video.data.utils.name
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.events.RecordingResult
import ru.mobileup.samples.features.video.domain.events.VideoRecorderEvent
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.presentation.recorder.controller.VideoRecorderController
import ru.mobileup.samples.features.video.presentation.recorder.widgets.CameraEffectIcon
import ru.mobileup.samples.features.video.presentation.recorder.widgets.CameraFlipIcon
import ru.mobileup.samples.features.video.presentation.recorder.widgets.FocusIndicator
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderButton
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderFilterSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderFpsSelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderQualitySelector
import ru.mobileup.samples.features.video.presentation.recorder.widgets.RecorderTorchSelector
import java.util.concurrent.TimeUnit

@Composable
fun VideoRecorderUi(
    component: VideoRecorderComponent,
    modifier: Modifier = Modifier,
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

    var cameraPlaceHolder: Pair<Bitmap?, Boolean> by remember {
        mutableStateOf(null to false)
    }

    val filtersPagerState = rememberPagerState(
        initialPage = 0,
    ) { availableFilters.size }

    val videoRecorderController by remember(context, lifecycleOwner, previewView) {
        mutableStateOf(
            VideoRecorderController(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                onVideoRecorderEvent = {
                    when (it) {
                        VideoRecorderEvent.StartRecord -> {
                            component.onUpdateIsRecording(true)
                            component.onUpdateConfig(RecorderConfig.Off)
                        }

                        is VideoRecorderEvent.ProgressRecord -> {
                            component.onUpdateDuration(it.recordDuration)
                        }

                        is VideoRecorderEvent.StopRecord -> {
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
                onPlaceHolderUpdated = {
                    cameraPlaceHolder = Pair(
                        first = it ?: cameraPlaceHolder.first,
                        second = it != null
                    )
                }
            )
        )
    }

    val previewTransformableState = rememberTransformableState { zoomChange, _, _ ->
        videoRecorderController.zoomChange(zoomChange = zoomChange)
    }

    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
        navigationBarIconsColor = SystemBarIconsColor.Light
    )

    LaunchedEffect(filtersPagerState.settledPage, filtersPagerState.isScrollInProgress) {
        val newEffectIndex = filtersPagerState.settledPage % availableFilters.size
        if (videoRecorderController.glFilter.ordinal != newEffectIndex && !filtersPagerState.isScrollInProgress) {
            val filter = availableFilters[newEffectIndex]
            videoRecorderController.glFilter = filter
        }
    }

    LaunchedEffect(recorderState) {
        videoRecorderController.recorderState = recorderState
    }

    DisposableEffect(videoRecorderController) {
        onDispose {
            videoRecorderController.release()
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
        cameraPlaceHolder = cameraPlaceHolder,
        videoRecorderController = videoRecorderController,
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
    cameraPlaceHolder: Pair<Bitmap?, Boolean>,
    videoRecorderController: VideoRecorderController,
    onUpdateConfig: (RecorderConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    val recorderDurationString by remember(recorderState.durationMs) {
        derivedStateOf { formatMillisToMS(recorderState.durationMs) }
    }

    val fling = PagerDefaults.flingBehavior(
        state = filtersPagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(15)
    )

    val blurStrength = remember { Animatable(0f) }

    var focusPoint by remember {
        mutableStateOf(Offset(-1f, -1f))
    }

    fun animateBlur() {
        scope.launch {
            blurStrength.snapTo(0f)
            blurStrength.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.palette.black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CustomTheme.colors.palette.black)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .statusBarsPadding()
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(R.string.video_menu_item_recorder),
                        color = CustomTheme.colors.palette.white,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = recorderState.fps.toString(),
                    color = if (recorderConfig == RecorderConfig.FPS) {
                        CustomTheme.colors.text.warning
                    } else {
                        CustomTheme.colors.palette.white
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onUpdateConfig(RecorderConfig.FPS)
                        }
                )

                Text(
                    text = recorderState.quality.name(),
                    color = if (recorderConfig == RecorderConfig.Quality) {
                        CustomTheme.colors.text.warning
                    } else {
                        CustomTheme.colors.palette.white
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
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
                        .size(24.dp)
                        .clickable {
                            onUpdateConfig(RecorderConfig.Torch)
                        }
                )
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures {
                        val factory = previewView.meteringPointFactory
                        val point = factory.createPoint(it.x, it.y)
                        val action = FocusMeteringAction.Builder(point).apply {
                            setAutoCancelDuration(3, TimeUnit.SECONDS)
                        }.build()
                        videoRecorderController.focusChange(action)
                        onUpdateConfig(RecorderConfig.Off)

                        focusPoint = it
                    }
                }
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .transformable(previewTransformableState),
                    factory = remember { { previewView } },
                )

                this@Column.AnimatedVisibility(
                    visible = cameraPlaceHolder.second,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    cameraPlaceHolder.first?.let {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .blur((blurStrength.value * 32.dp)),
                            bitmap = it.asImageBitmap(),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }
                }

                FocusIndicator(
                    offset = focusPoint,
                    onExposureChange = {
                        videoRecorderController.exposureChange(it)
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .clickableNoRipple {
                    // Do nothing
                }
                .noOverlapByMessage()
        ) {
            Box {
                RecorderFpsSelector(
                    recorderConfig = recorderConfig,
                    fps = recorderState.fps,
                    onFpsSelect = {
                        component.onUpdateFps(it)
                        animateBlur()
                    }
                )

                RecorderQualitySelector(
                    recorderConfig = recorderConfig,
                    quality = recorderState.quality,
                    onQualitySelect = {
                        component.onUpdateQuality(it)
                        animateBlur()
                    }
                )

                RecorderTorchSelector(
                    recorderConfig = recorderConfig,
                    torchState = recorderState.torchState,
                    onTorchSelect = {
                        component.onUpdateTorchState(it)
                        videoRecorderController.changeTorchState(it)
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
                    .padding(16.dp)
                    .animateContentSize(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Max)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        CameraEffectIcon(
                            recorderConfig = recorderConfig,
                            onClick = {
                                onUpdateConfig(RecorderConfig.Filter)
                            }
                        )
                    }

                    RecorderButton(
                        isRecording = recorderState.isRecording,
                        onClick = {
                            if (recorderState.isRecording) {
                                if (recorderState.durationMs < 1000) {
                                    component.onRecordStopFailed()
                                } else {
                                    videoRecorderController.stopRecording()
                                }
                            } else {
                                videoRecorderController.startRecording()
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        CameraFlipIcon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(Alignment.CenterStart),
                            onClick = {
                                component.onFlipCameraSelector()
                                animateBlur()
                            }
                        )
                    }
                }
                Crossfade(recorderState.isRecording) {
                    if (it) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp),
                            text = recorderDurationString,
                            color = CustomTheme.colors.text.invert
                        )
                    }
                }
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