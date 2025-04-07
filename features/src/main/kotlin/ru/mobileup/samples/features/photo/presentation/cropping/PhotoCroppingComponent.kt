package ru.mobileup.samples.features.photo.presentation.cropping

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface PhotoCroppingComponent {
    val pickedImageUri: StateFlow<Uri?>

    fun onPickPhoto(uri: Uri)
}