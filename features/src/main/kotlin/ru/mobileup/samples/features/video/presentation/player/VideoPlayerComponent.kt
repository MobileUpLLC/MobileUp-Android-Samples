package ru.mobileup.samples.features.video.presentation.player

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.features.video.data.render.GlFilter
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.domain.states.PlayerState
import ru.mobileup.samples.features.video.domain.VideoTransform

interface VideoPlayerComponent {

    val uri: Uri

    val playerConfig: StateFlow<PlayerConfig>

    val playerState: StateFlow<PlayerState>

    val resetTransformDialog: StandardDialogControl

    val saveDialog: StandardDialogControl

    fun onUpdateConfig(playerConfig: PlayerConfig)

    fun onSaveClick()

    fun onUpdateVolume(volume: Float)

    fun onUpdateSpeed(speed: Float)

    fun onUpdateVideoTransform(videoTransform: VideoTransform)

    fun onResetVideoTransform()

    fun onCut(startPositionMs: Long, endPositionMs: Long)

    fun onUpdateFilter(glFilter: GlFilter)

    fun onCancelRender()
}