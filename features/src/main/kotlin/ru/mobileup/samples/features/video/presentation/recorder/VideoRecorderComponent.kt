package ru.mobileup.samples.features.video.presentation.recorder

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.video.domain.states.RecorderState
import ru.mobileup.samples.features.video.domain.RecorderConfig

interface VideoRecorderComponent {

    val recorderConfig: StateFlow<RecorderConfig>

    val recorderState: StateFlow<RecorderState>

    fun onUpdateConfig(recorderConfig: RecorderConfig)

    fun onUpdateIsRecording(isRecording: Boolean)

    fun onUpdateDuration(durationMs: Long)

    fun onRecordInitializationFailed()

    fun onRecordCompleted(uri: Uri)

    fun onRecordFailed()

    fun onRecordStopFailed()

    fun onFlipCameraSelector()

    fun onUpdateFps(fps: Int)

    fun onUpdateQuality(quality: Quality)

    fun onUpdateTorchState(torchState: Boolean)

    sealed interface Output {
        data class RecordCompleted(val uri: Uri) : Output
    }
}