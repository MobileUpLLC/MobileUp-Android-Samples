package ru.mobileup.samples.features.video.presentation.recorder

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.video.Quality
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.RecorderConfig
import ru.mobileup.samples.features.video.domain.states.RecorderState

class RealVideoRecorderComponent(
    componentContext: ComponentContext,
    private val messageService: MessageService,
    private val onOutput: (VideoRecorderComponent.Output) -> Unit
) : ComponentContext by componentContext, VideoRecorderComponent {

    override val recorderConfig = MutableStateFlow(RecorderConfig.Off)

    override val recorderState = MutableStateFlow(RecorderState.build())

    override fun onUpdateConfig(recorderConfig: RecorderConfig) {
        this.recorderConfig.update {
            recorderConfig
        }
    }

    override fun onUpdateIsRecording(isRecording: Boolean) {
        recorderState.update {
            it.copy(isRecording = isRecording)
        }
    }

    override fun onUpdateDuration(durationMs: Long) {
        recorderState.update {
            it.copy(durationMs = durationMs)
        }
    }

    override fun onRecordInitializationFailed() {
        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.recorder_initialization_failed
                )
            )
        )
    }

    override fun onRecordCompleted(uri: Uri) {
        onOutput(VideoRecorderComponent.Output.RecordCompleted(uri))
    }

    override fun onRecordFailed() {
        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.recorder_failed
                )
            )
        )
    }

    override fun onRecordStopFailed() {
        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.recorder_stop_failed
                )
            )
        )
    }

    override fun onFlipCameraSelector() {
        recorderState.update {
            it.copy(
                cameraSelector = if (it.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            )
        }
    }

    override fun onUpdateFps(fps: Int) {
        recorderState.update {
            it.copy(fps = fps)
        }
    }

    override fun onUpdateQuality(quality: Quality) {
        recorderState.update {
            it.copy(quality = quality)
        }
    }

    override fun onUpdateTorchState(torchState: Boolean) {
        recorderState.update {
            it.copy(torchState = torchState)
        }
    }
}