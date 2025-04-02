package ru.mobileup.samples.features.photo.presentation.camera

import android.net.Uri
import androidx.camera.core.CameraSelector
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.photo.domain.states.CameraState

class RealPhotoCameraComponent(
    componentContext: ComponentContext,
    private val onOutput: (PhotoCameraComponent.Output) -> Unit,
    private val messageService: MessageService
) : ComponentContext by componentContext, PhotoCameraComponent {

    override val cameraState = MutableStateFlow(CameraState.build())

    override fun onCameraInitializationFailed() {
        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.camera_initialization_failed
                )
            )
        )
    }

    override fun onPhotoTaken(uri: Uri) {
        cameraState.update {
            it.copy(
                uris = it.uris + uri
            )
        }
    }

    override fun onPhotoFailed() {
        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.photo_failed
                )
            )
        )
    }

    override fun onShowPreview() {
        onOutput(PhotoCameraComponent.Output.PreviewRequested(cameraState.value.uris))
    }

    override fun onFlipCameraSelector() {
        cameraState.update {
            it.copy(
                cameraSelector = if (it.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            )
        }
    }

    override fun onUpdateTorchState() {
        cameraState.update {
            it.copy(torchState = !cameraState.value.torchState)
        }
    }
}