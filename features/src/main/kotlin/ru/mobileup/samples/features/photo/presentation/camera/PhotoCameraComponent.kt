package ru.mobileup.samples.features.photo.presentation.camera

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.photo.domain.states.CameraState

interface PhotoCameraComponent {

    val cameraState: StateFlow<CameraState>

    fun onCameraInitializationFailed()

    fun onPhotoTaken(uri: Uri)

    fun onPhotoFailed()

    fun onShowPreview()

    fun onFlipCameraSelector()

    fun onUpdateTorchState()

    sealed interface Output {
        data class PreviewRequested(val uris: List<Uri>) : Output
    }
}