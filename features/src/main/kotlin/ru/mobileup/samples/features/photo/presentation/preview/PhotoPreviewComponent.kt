package ru.mobileup.samples.features.photo.presentation.preview

import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.features.image.presentation.carousel.ImageCarouselComponent

interface PhotoPreviewComponent {

    val imageCarouselComponent: ImageCarouselComponent

    val saveDialog: StandardDialogControl

    fun onSaveClick()
}