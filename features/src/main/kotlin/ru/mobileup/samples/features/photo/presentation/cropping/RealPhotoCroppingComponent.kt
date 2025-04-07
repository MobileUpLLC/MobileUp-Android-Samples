package ru.mobileup.samples.features.photo.presentation.cropping

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow

class RealPhotoCroppingComponent(
    val componentContext: ComponentContext
) : ComponentContext by componentContext, PhotoCroppingComponent {

    override val pickedImageUri: MutableStateFlow<Uri?> = MutableStateFlow(null)

    override fun onPickPhoto(uri: Uri) {
        pickedImageUri.value = uri
    }
}