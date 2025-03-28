package ru.mobileup.samples.features.photo.domain.states

import android.net.Uri
import androidx.camera.core.CameraSelector

data class CameraState(
    val mediaList: List<Uri>,
    val cameraSelector: CameraSelector,
    val torchState: Boolean
) {
    companion object {
        fun build() = CameraState(
            mediaList = listOf(),
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
            torchState = false
        )
    }
}