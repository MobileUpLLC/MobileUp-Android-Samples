@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mobileup.samples.features.video.presentation.player

import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.UnstableApi
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.delay
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.message.presentation.noOverlapByMessage
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.HideWindowInsetsEffect
import ru.mobileup.samples.core.utils.LockScreenOrientation
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.core.utils.formatMillisToMS
import ru.mobileup.samples.core.utils.zeroWindowInsets
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.data.render.availableFilters
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.domain.rotate
import ru.mobileup.samples.features.video.domain.states.PlayerState
import ru.mobileup.samples.features.video.presentation.player.preview.PlayerOrientation
import ru.mobileup.samples.features.video.presentation.player.preview.PlayerSurfaceView
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerCropSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerCutSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerFilterSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerIndication
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerSpeedSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.PlayerVolumeSelector
import ru.mobileup.samples.features.video.presentation.player.widgets.rememberPlayerIndicationState

private const val HELPING_OFFSET_MULTIPLIER = 2.5f

@UnstableApi
@Composable
fun VideoPlayerUi(
    component: VideoPlayerComponent,
    modifier: Modifier = Modifier,
) {
    val playerConfig by component.playerConfig.collectAsState()

    val playerState by component.playerState.collectAsState()

    val filtersPagerState = rememberPagerState(
        initialPage = 0,
    ) { availableFilters.size }

    VideoPlayerContent(
        component = component,
        playerConfig = playerConfig,
        playerState = playerState,
        filtersPagerState = filtersPagerState,
        modifier = modifier
    )

    val changeSystemBarColor = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    if (changeSystemBarColor) {
        SystemBars(
            statusBarColor = Color.Transparent,
            navigationBarColor = Color.Transparent,
            statusBarIconsColor = SystemBarIconsColor.Light,
            navigationBarIconsColor = SystemBarIconsColor.Light
        )
    }

    StandardDialog(component.resetTransformDialog)
    StandardDialog(component.saveDialog)
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayerContent(
    component: VideoPlayerComponent,
    playerConfig: PlayerConfig,
    playerState: PlayerState,
    filtersPagerState: PagerState,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val density = LocalDensity.current

    val localConfiguration = LocalConfiguration.current

    val playerSize by remember {
        mutableStateOf(
            Size(
                width = with(density) { localConfiguration.screenWidthDp.dp.toPx() },
                height = with(density) { localConfiguration.screenHeightDp.dp.toPx() },
            )
        )
    }

    val transformationState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
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

    val playerView by remember {
        mutableStateOf(PlayerSurfaceView(context))
    }

    LaunchedEffect(filtersPagerState.settledPage, filtersPagerState.isScrollInProgress) {
        val newEffectIndex = filtersPagerState.settledPage % availableFilters.size
        if (playerView.glFilter.ordinal != newEffectIndex && !filtersPagerState.isScrollInProgress) {
            val filter = availableFilters[newEffectIndex]
            component.onUpdateFilter(filter)
            playerView.setFilter(filter)
        }
    }

    LaunchedEffect(component.videoPlayerController.player) {
        playerView.setExoPlayer(component.videoPlayerController.player)
    }

    LaunchedEffect(playerState.videoTransform, playerView) {
        playerView.setTransform(playerState.videoTransform)
    }

    val isPlaying by component.isPlaying.collectAsState()

    val isFullScreen by remember(playerState.orientation) {
        mutableStateOf(playerState.orientation is PlayerOrientation.Landscape)
    }

    val videoProgress by component.videoPlayerController.progressState.collectAsState()

    LockScreenOrientation(
        if (isFullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            Crossfade(
                targetState = playerState.orientation
            ) { orientation ->
                if (orientation is PlayerOrientation.Portrait) {
                    PlayerTopBar(
                        config = playerConfig,
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
                        },
                        onSaveClick = component::onSaveClick,
                        onShareClick = component::onShareClick
                    )
                }
            }
        },
        bottomBar = {
            Crossfade(
                modifier = Modifier
                    .fillMaxWidth(),
                targetState = playerState.orientation
            ) { orientation ->
                when (orientation) {
                    PlayerOrientation.Landscape -> {
                        PlayerLandscapeBottomBar(
                            playerState = playerState,
                            playerConfig = playerConfig,
                            component = component,
                            progress = { videoProgress },
                            onProgressChange = component.videoPlayerController::setProgress,
                            onChangeOrientation = component::onChangeOrientation
                        )
                    }
                    PlayerOrientation.Portrait -> {
                        AnimatedVisibility(
                            visible = playerState.renderProgress == null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            PlayerPortraitBottomBar(
                                config = playerConfig,
                                playerState = playerState,
                                filtersPagerState = filtersPagerState,
                                component = component,
                                progress = { videoProgress },
                                onProgressChange = component.videoPlayerController::setProgress,
                                onChangeOrientation = component::onChangeOrientation
                            )
                        }
                    }
                }
            }
        },
        contentWindowInsets = if (isFullScreen) {
            zeroWindowInsets
        } else {
            WindowInsets.statusBars
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CustomTheme.colors.palette.black)
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            Player(
                modifier = Modifier
                    .fillMaxSize(),
                config = playerConfig,
                isPlaying = isPlaying,
                playerView = playerView,
                transformationState = transformationState,
                onPause = component.videoPlayerController::pause,
                onPlay = component.videoPlayerController::play,
            )
        }
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
private fun PlayerPortraitBottomBar(
    config: PlayerConfig,
    playerState: PlayerState,
    filtersPagerState: PagerState,
    component: VideoPlayerComponent,
    progress: () -> Float,
    onProgressChange: (Float) -> Unit,
    onChangeOrientation: () -> Unit,
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

        VideoProgressBar(
            playerState = playerState,
            playerConfig = config,
            progress = progress,
            onProgressChange = onProgressChange,
            onChangeOrientation = onChangeOrientation,
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(horizontal = 12.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun PlayerLandscapeBottomBar(
    playerState: PlayerState,
    playerConfig: PlayerConfig,
    component: VideoPlayerComponent,
    progress: () -> Float,
    onProgressChange: (Float) -> Unit,
    onChangeOrientation: () -> Unit,
) {

    val isPlaying by component.isPlaying.collectAsState()

    val safeContentPaddings = WindowInsets.safeContent.asPaddingValues()

    val layoutDirection = LocalLayoutDirection.current

    var isVisibleProgressBar by remember(isPlaying) {
        mutableStateOf(true)
    }

    LaunchedEffect(isVisibleProgressBar, isPlaying) {
        if (isVisibleProgressBar && isPlaying) {
            delay(3_000)
            isVisibleProgressBar = false
        }
    }

    HideWindowInsetsEffect(
        isHidden = !isVisibleProgressBar,
        insetsTypes = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    )

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth(),
        visible = isVisibleProgressBar,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        VideoProgressBar(
            playerState = playerState,
            playerConfig = playerConfig,
            onProgressChange = onProgressChange,
            onChangeOrientation = onChangeOrientation,
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50)
                .padding(
                    start = safeContentPaddings.calculateStartPadding(layoutDirection),
                    end = safeContentPaddings.calculateEndPadding(layoutDirection),
                    bottom = safeContentPaddings.calculateBottomPadding()
                )
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
private fun VideoProgressBar(
    playerState: PlayerState,
    playerConfig: PlayerConfig,
    progress: () -> Float,
    onProgressChange: (Float) -> Unit,
    onChangeOrientation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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
            color = CustomTheme.colors.palette.white,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        if (playerConfig == PlayerConfig.Off) {
            IconButton(
                onClick = onChangeOrientation,
            ) {
                val orientationIcon by remember(playerState.orientation) {
                    mutableIntStateOf(
                        when (playerState.orientation) {
                            PlayerOrientation.Landscape -> R.drawable.ic_close_fullscreen
                            PlayerOrientation.Portrait -> R.drawable.ic_open_fullscreen
                        }
                    )
                }

                Icon(
                    painter = painterResource(orientationIcon),
                    tint = CustomTheme.colors.palette.white,
                    contentDescription = "orientation_icon"
                )
            }
        }
    }
}

@UnstableApi
@Preview
@Composable
private fun VideoPlayerUiPreview() {
    AppTheme {
        VideoPlayerUi(FakeVideoPlayerComponent(LocalContext.current))
    }
}
