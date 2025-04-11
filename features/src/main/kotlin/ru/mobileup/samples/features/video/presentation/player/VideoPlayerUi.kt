@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mobileup.samples.features.video.presentation.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.message.presentation.noOverlapByMessage
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.data.render.availableFilters
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.domain.events.VideoPlayerEvent
import ru.mobileup.samples.features.video.domain.rotate
import ru.mobileup.samples.features.video.domain.states.PlayerState
import ru.mobileup.samples.features.video.presentation.player.controller.VideoPlayerController
import ru.mobileup.samples.features.video.presentation.player.preview.PlayerSurfaceView
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerCropSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerCutSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerFilterSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerIndication
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerSpeedSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerVolumeSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.RenderProgressIndicatorOverlay
import ru.mobileup.samples.features.video.presentation.player.widgets.rememberPlayerIndicationState

private const val HELPING_OFFSET_MULTIPLIER = 2.5f

@UnstableApi
@Composable
fun VideoPlayerUi(
    component: VideoPlayerComponent,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val playerConfig by component.playerConfig.collectAsState()
    val playerState by component.playerState.collectAsState()
    val density = LocalDensity.current
    val localConfiguration = LocalConfiguration.current

    var isPlaying by remember {
        mutableStateOf(false)
    }

    var isFirstLaunch by remember {
        mutableStateOf(true)
    }

    val filtersPagerState = rememberPagerState(
        initialPage = 0,
    ) { availableFilters.size }

    val videoPlayerController by remember(context) {
        mutableStateOf(
            VideoPlayerController(
                context = context,
                onVideoPlayerEvent = { playerEvent, _ ->
                    when (playerEvent) {
                        is VideoPlayerEvent.PlayingStateChanged -> {
                            isPlaying = playerEvent.isPlaying
                        }
                    }
                }
            )
        )
    }

    val playerView by remember {
        mutableStateOf(
            PlayerSurfaceView(context)
        )
    }

    val playerSize by remember {
        mutableStateOf(
            Size(
                width = with(density) { localConfiguration.screenWidthDp.dp.toPx() },
                height = with(density) { localConfiguration.screenHeightDp.dp.toPx() },
            )
        )
    }

    val transformationState =
        rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            if (playerConfig != PlayerConfig.Crop) {
                return@rememberTransformableState
            }

            val allowedScaleRange = 0.5f..2f
            val transform = playerState.videoTransform
            val newScale = (transform.scale * zoomChange).run {
                if (this !in allowedScaleRange) {
                    transform.scale
                } else {
                    this
                }
            }
            val newRotation = transform.rotate(rotationChange)
            val rotatedOffset =
                offsetChange.rotate(newRotation) * newScale * HELPING_OFFSET_MULTIPLIER

            component.onUpdateVideoTransform(
                transform.copy(
                    scale = newScale,
                    rotation = newRotation,
                    offsetPercent = Offset(
                        x = transform.offsetPercent.x + (rotatedOffset.x / playerSize.width),
                        y = transform.offsetPercent.y + (rotatedOffset.y / playerSize.height)
                    )
                )
            )
        }

    LaunchedEffect(filtersPagerState.settledPage, filtersPagerState.isScrollInProgress) {
        val newEffectIndex = filtersPagerState.settledPage % availableFilters.size
        if (playerView.glFilter.ordinal != newEffectIndex && !filtersPagerState.isScrollInProgress) {
            val filter = availableFilters[newEffectIndex]
            component.onUpdateFilter(filter)
            playerView.setFilter(filter)
        }
    }

    LaunchedEffect(playerState.volume) {
        videoPlayerController.setVolume(playerState.volume)
    }

    LaunchedEffect(playerState.speed) {
        videoPlayerController.setSpeed(playerState.speed)
    }

    LaunchedEffect(playerState.startPositionMs, playerState.endPositionMs) {
        videoPlayerController.setMedia(
            uri = component.uri,
            startPositionMs = playerState.startPositionMs,
            endPositionMs = playerState.endPositionMs
        )

        if (isFirstLaunch) {
            videoPlayerController.play()
            isFirstLaunch = false
        }
    }

    LaunchedEffect(videoPlayerController.player) {
        playerView.setExoPlayer(videoPlayerController.player)
    }

    LaunchedEffect(playerState.videoTransform, playerView) {
        playerView.setTransform(playerState.videoTransform)
    }

    DisposableEffect(Unit) {
        onDispose {
            videoPlayerController.release()
        }
    }

    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
        navigationBarIconsColor = SystemBarIconsColor.Light
    )

    StandardDialog(component.resetTransformDialog)
    StandardDialog(component.saveDialog)

    VideoPlayerContent(
        modifier = modifier,
        component = component,
        playerConfig = playerConfig,
        playerState = playerState,
        transformationState = transformationState,
        filtersPagerState = filtersPagerState,
        playerView = playerView,
        isPlaying = isPlaying,
        videoPlayerController = videoPlayerController,
        onUpdateConfig = {
            if (playerState.renderProgress == null) {
                component.onUpdateConfig(
                    if (playerConfig == it) {
                        PlayerConfig.Off
                    } else {
                        it
                    }
                )
            }
        }
    )
}

@UnstableApi
@Composable
private fun VideoPlayerContent(
    component: VideoPlayerComponent,
    playerConfig: PlayerConfig,
    playerState: PlayerState,
    transformationState: TransformableState,
    filtersPagerState: PagerState,
    playerView: PlayerSurfaceView,
    isPlaying: Boolean,
    videoPlayerController: VideoPlayerController,
    onUpdateConfig: (PlayerConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    val videoProgress by videoPlayerController.progressState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            PlayerTopBar(
                config = playerConfig,
                onUpdateConfig = onUpdateConfig,
                onSaveClick = component::onSaveClick,
                onShareClick = component::onShareClick
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = playerState.renderProgress == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PlayerBottomBar(
                    config = playerConfig,
                    playerState = playerState,
                    filtersPagerState = filtersPagerState,
                    component = component,
                    progress = { videoProgress },
                    onProgressChange = videoPlayerController::setProgress
                )
            }
        }
    ) { paddingValues ->
        Player(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomTheme.colors.palette.black)
                .padding(top = paddingValues.calculateTopPadding()),
            config = playerConfig,
            isPlaying = isPlaying,
            playerView = playerView,
            transformationState = transformationState,
            onPause = videoPlayerController::pause,
            onPlay = videoPlayerController::play,
        )
    }

    AnimatedVisibility(
        visible = playerState.renderProgress != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RenderProgressIndicatorOverlay(
            progress = { playerState.renderProgress ?: 1f },
            onCancel = component::onCancelRender
        )
    }
}

@Composable
private fun Player(
    config: PlayerConfig,
    isPlaying: Boolean,
    playerView: PlayerSurfaceView,
    transformationState: TransformableState,
    onPause: () -> Unit,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val playingIndicationState by rememberPlayerIndicationState(isPlaying = isPlaying)

    val animatedPlayerWeight by animateFloatAsState(
        targetValue = if (config == PlayerConfig.Crop) 0.6f else 1f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "playerWeight"
    )

    Box(
        modifier = modifier
    ) {
        AndroidView(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = animatedPlayerWeight
                    scaleY = animatedPlayerWeight
                    translationY = -(size.height - size.height * animatedPlayerWeight) / 4
                }
                .matchParentSize()
                .clickableNoRipple(listener = if (isPlaying) onPause else onPlay)
                .transformable(transformationState),
            factory = remember { { playerView } },
        )

        PlayerIndication(
            modifier = Modifier.align(Alignment.Center),
            state = playingIndicationState,
            isVisible = true
        )
    }
}

@Composable
private fun PlayerTopBar(
    config: PlayerConfig,
    onUpdateConfig: (PlayerConfig) -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.palette.black)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = stringResource(R.string.video_menu_item_player),
                color = CustomTheme.colors.palette.white,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_volume),
            contentDescription = "volume",
            tint = if (config == PlayerConfig.Volume) {
                CustomTheme.colors.icon.warning
            } else {
                Color.Unspecified
            },
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Volume)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_speed),
            contentDescription = "speed",
            tint = if (config == PlayerConfig.Speed) {
                CustomTheme.colors.icon.warning
            } else {
                Color.Unspecified
            },
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Speed)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_framing),
            contentDescription = "crop",
            tint = if (config == PlayerConfig.Crop) {
                CustomTheme.colors.icon.warning
            } else {
                Color.Unspecified
            },
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Crop)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_cut),
            contentDescription = "cut",
            tint = if (config == PlayerConfig.Cut) {
                CustomTheme.colors.icon.warning
            } else {
                Color.Unspecified
            },
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Cut)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "filter",
            tint = if (config == PlayerConfig.Filter) {
                CustomTheme.colors.icon.warning
            } else {
                Color.Unspecified
            },
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Filter)
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = "download",
            tint = Color.Unspecified,
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Off)
                onSaveClick()
            }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_share),
            contentDescription = "share",
            tint = Color.Unspecified,
            modifier = Modifier.clickable {
                onUpdateConfig(PlayerConfig.Off)
                onShareClick()
            }
        )
    }
}

@Composable
private fun PlayerBottomBar(
    config: PlayerConfig,
    playerState: PlayerState,
    filtersPagerState: PagerState,
    component: VideoPlayerComponent,
    progress: () -> Float,
    onProgressChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .noOverlapByMessage()
    ) {
        Box {
            PlayerVolumeSelector(
                playerConfig = config,
                volume = playerState.volume,
                onUpdateVolume = component::onUpdateVolume
            )

            PlayerSpeedSelector(
                playerConfig = config,
                speed = playerState.speed,
                onUpdateSpeed = component::onUpdateSpeed
            )

            PlayerCropSelector(
                playerConfig = config,
                onCompleteClick = {
                    component.onUpdateConfig(PlayerConfig.Off)
                },
                onResetClick = {
                    component.onResetVideoTransform()
                }
            )

            PlayerCutSelector(
                playerConfig = config,
                startPositionMs = playerState.startPositionMs,
                endPositionMs = playerState.endPositionMs,
                maxDurationMs = playerState.maxDurationMs,
                speed = playerState.speed,
                onCut = component::onCut
            )

            val fling = PagerDefaults.flingBehavior(
                state = filtersPagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(15)
            )

            PlayerFilterSelector(
                playerConfig = config,
                filtersPagerState = filtersPagerState,
                fling = fling
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(horizontal = 12.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = formatMillisToMS((progress() * playerState.duration).toLong()),
                color = CustomTheme.colors.text.invert,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
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
                thumb = { /*Nothing*/ },
                value = progress(),
                onValueChange = onProgressChange
            )

            Text(
                text = formatMillisToMS(playerState.duration),
                color = CustomTheme.colors.text.invert,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@UnstableApi
@Preview
@Composable
private fun VideoPlayerUiPreview() {
    AppTheme {
        VideoPlayerUi(FakeVideoPlayerComponent())
    }
}
