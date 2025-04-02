package ru.mobileup.samples.features.video.presentation.player

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.dialog.standard.fakeStandardDialogControl
import ru.mobileup.samples.features.video.data.render.GlFilter
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.domain.states.PlayerState
import ru.mobileup.samples.features.video.domain.VideoTransform

class FakeVideoPlayerComponent : VideoPlayerComponent {

    override val uri: Uri = Uri.EMPTY

    override val playerConfig = MutableStateFlow(PlayerConfig.Off)

    override val playerState: StateFlow<PlayerState> = MutableStateFlow(
        PlayerState.build()
    )

    override val resetTransformDialog: StandardDialogControl = fakeStandardDialogControl()

    override val saveDialog: StandardDialogControl = fakeStandardDialogControl()

    override fun onUpdateConfig(playerConfig: PlayerConfig) = Unit

    override fun onSaveClick() = Unit

    override fun onUpdateVolume(volume: Float) = Unit

    override fun onUpdateSpeed(speed: Float) = Unit

    override fun onUpdateVideoTransform(videoTransform: VideoTransform) = Unit

    override fun onResetVideoTransform() = Unit

    override fun onCut(startPositionMs: Long, endPositionMs: Long) = Unit

    override fun onUpdateFilter(glFilter: GlFilter) = Unit

    override fun onCancelRender() = Unit
}