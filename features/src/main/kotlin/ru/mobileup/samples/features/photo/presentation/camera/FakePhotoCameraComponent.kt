package ru.mobileup.samples.features.photo.presentation.camera

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.photo.domain.states.CameraState

class FakePhotoCameraComponent : PhotoCameraComponent {
    override val cameraState = MutableStateFlow(CameraState.build())
    override fun onCameraInitializationFailed() = Unit
    override fun onPhotoTaken(uri: Uri) = Unit
    override fun onPhotoFailed() = Unit
    override fun onShowPreview() = Unit
    override fun onFlipCameraSelector() = Unit
    override fun onUpdateTorchState() = Unit
}