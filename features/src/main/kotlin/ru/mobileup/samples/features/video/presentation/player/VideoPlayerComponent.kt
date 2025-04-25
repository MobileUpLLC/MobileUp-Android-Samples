package ru.mobileup.samples.features.video.presentation.player

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.features.video.data.render.GlFilter
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.domain.VideoTransform
import ru.mobileup.samples.features.video.domain.states.PlayerState
import ru.mobileup.samples.features.video.presentation.player.controller.VideoPlayerController

interface VideoPlayerComponent {

    val videoPlayerController: VideoPlayerController

    val isPlaying: StateFlow<Boolean>

    val playerConfig: StateFlow<PlayerConfig>

    val playerState: StateFlow<PlayerState>

    val resetTransformDialog: StandardDialogControl

    val saveDialog: StandardDialogControl

    fun onUpdateConfig(playerConfig: PlayerConfig)

    fun onUpdateVolume(volume: Float)

    fun onUpdateSpeed(speed: Float)

    fun onUpdateVideoTransform(videoTransform: VideoTransform)

    fun onResetVideoTransform()

    fun onCut(startPositionMs: Long, endPositionMs: Long)

    fun onUpdateFilter(glFilter: GlFilter)

    fun onChangeOrientation()

    fun onSaveClick()

    fun onShareClick()

    fun onCancelRender()
}