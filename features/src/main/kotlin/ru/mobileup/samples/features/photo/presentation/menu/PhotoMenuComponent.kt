package ru.mobileup.samples.features.photo.presentation.menu

import android.net.Uri

interface PhotoMenuComponent {

    fun onCameraClick()

    fun onPreviewClick(uris: List<Uri>)

    fun onCroppingClick()

    sealed interface Output {
        data object CameraRequested : Output
        data object CroppingRequested : Output
        data class PreviewRequested(val uris: List<Uri>) : Output
    }
}