package ru.mobileup.samples.features.image.presentation.carousel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.image.domain.ImageCarousel
import ru.mobileup.samples.features.image.domain.ImageResource

class FakeImageCarouselComponent : ImageCarouselComponent {

    override val imageCarousel: StateFlow<ImageCarousel> = MutableStateFlow(ImageCarousel(
        ImageResource.MOCK_LIST,
        0
    ))
    override val mode: StateFlow<ImageCarouselMode> = MutableStateFlow(ImageCarouselMode.Fullscreen)

    override fun onCarouselPageChanged(position: Int) = Unit
    override fun onImageClick() = Unit
}