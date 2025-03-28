package ru.mobileup.samples.features.photo.presentation.menu

import android.net.Uri

class FakePhotoMenuComponent : PhotoMenuComponent {
    override fun onCameraClick() = Unit
    override fun onPreviewClick(mediaList: List<Uri>) = Unit
}