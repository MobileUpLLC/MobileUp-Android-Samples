package ru.mobileup.samples.features.image.presentation.carousel

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.image.domain.ImageCarousel
import ru.mobileup.samples.core.media.ImageResource

class RealImageCarouselComponent(
    imageResources: List<ImageResource>,
    componentContext: ComponentContext
) : ComponentContext by componentContext, ImageCarouselComponent {

    override val imageCarousel: MutableStateFlow<ImageCarousel> = MutableStateFlow(
        ImageCarousel(
            imageResources = imageResources,
            currentImagePosition = 0
        )
    )

    override val mode: MutableStateFlow<ImageCarouselMode> = MutableStateFlow(ImageCarouselMode.Embedded)

    init {
        val backCallback = BackCallback {
            if (mode.value == ImageCarouselMode.Fullscreen) {
                mode.update { ImageCarouselMode.Embedded }
            }
        }

        backHandler.register(backCallback)

        mode
            .onEach {
                when (it) {
                    ImageCarouselMode.Embedded -> backHandler.unregister(backCallback)
                    ImageCarouselMode.Fullscreen -> backHandler.register(backCallback)
                }
            }
            .launchIn(componentScope)
    }

    override fun onCarouselPageChanged(position: Int) {
        imageCarousel.update { it.copy(currentImagePosition = position) }
    }

    override fun onImageClick() {
        mode.value = ImageCarouselMode.Fullscreen
    }
}