package ru.mobileup.samples.features.photo.presentation.menu

import android.net.Uri

class FakePhotoMenuComponent : PhotoMenuComponent {
    override fun onCameraClick() = Unit

    override fun onPreviewClick(uris: List<Uri>) = Unit

    override fun onCroppingClick() = Unit
}