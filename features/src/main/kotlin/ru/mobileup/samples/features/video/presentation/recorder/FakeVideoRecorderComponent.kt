package ru.mobileup.samples.features.video.presentation.recorder

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.domain.RecorderConfig

class FakeVideoRecorderComponent : VideoRecorderComponent {

    override val recorderConfig = MutableStateFlow(RecorderConfig.Off)

    override val recorderState = MutableStateFlow(RecorderState.build())

    override fun onUpdateConfig(recorderConfig: RecorderConfig) = Unit

    override fun onUpdateIsRecording(isRecording: Boolean) = Unit

    override fun onUpdateDuration(durationMs: Long) = Unit

    override fun onRecordInitializationFailed() = Unit

    override fun onRecordCompleted(uri: Uri) = Unit

    override fun onRecordFailed() = Unit

    override fun onRecordStopFailed() = Unit

    override fun onFlipCameraSelector() = Unit

    override fun onUpdateFps(fps: Int) = Unit

    override fun onUpdateQuality(quality: Quality) = Unit

    override fun onUpdateTorchState(torchState: Boolean) = Unit
}