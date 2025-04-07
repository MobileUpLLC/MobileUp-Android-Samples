package ru.mobileup.samples.features.photo.presentation.menu

import ru.mobileup.samples.features.photo.domain.PhotoMenu

import android.net.Uri

interface PhotoMenuComponent {

    fun onMenuClick(menuItem: PhotoMenu)

    fun onPreviewClick(uris: List<Uri>)

    sealed interface Output {
        data object CameraRequested : Output
        data object CroppingRequested : Output
        data class PreviewRequested(val uris: List<Uri>) : Output
    }
}