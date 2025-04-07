package ru.mobileup.samples.features.photo.presentation.menu

import ru.mobileup.samples.features.photo.domain.PhotoMenu

import android.net.Uri

class FakePhotoMenuComponent : PhotoMenuComponent {
    override fun onMenuClick(menuItem: PhotoMenu) = Unit
}