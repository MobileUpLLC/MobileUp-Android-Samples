package ru.mobileup.samples.features.photo.presentation.cropping

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow

class FakePhotoCroppingComponent : PhotoCroppingComponent {
    override val pickedImageUri: MutableStateFlow<Uri?> = MutableStateFlow(null)

    override fun onPickPhoto(uri: Uri) = Unit
}