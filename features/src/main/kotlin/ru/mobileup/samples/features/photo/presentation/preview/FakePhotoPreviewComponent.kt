package ru.mobileup.samples.features.photo.presentation.preview

import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.core.dialog.standard.fakeStandardDialogControl
import ru.mobileup.samples.features.image.presentation.carousel.FakeImageCarouselComponent
import ru.mobileup.samples.features.image.presentation.carousel.ImageCarouselComponent

class FakePhotoPreviewComponent : PhotoPreviewComponent {
    override val imageCarouselComponent: ImageCarouselComponent = FakeImageCarouselComponent()
    override val saveDialog: StandardDialogControl = fakeStandardDialogControl()
    override fun onSaveClick() = Unit
}