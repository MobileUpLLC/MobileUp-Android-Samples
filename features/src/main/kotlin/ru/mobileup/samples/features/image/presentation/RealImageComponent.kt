package ru.mobileup.samples.features.image.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.image.createImageCarouselComponent
import ru.mobileup.samples.features.image.domain.ImageResource
import ru.mobileup.samples.features.image.presentation.carousel.ImageCarouselComponent

class RealImageComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory
) : ComponentContext by componentContext, ImageComponent {

    override val imageCarouselComponent: ImageCarouselComponent =
        componentFactory.createImageCarouselComponent(
            imageResources = ImageResource.MOCK_LIST,
            childContext("imageCarousel")
        )
    override val title: StateFlow<StringDesc> = MutableStateFlow(
        R.string.image_carousel_title_cats.strResDesc()
    )

    override val description: StateFlow<StringDesc> = MutableStateFlow(
        R.string.image_carousel_description_cats.strResDesc()
    )
}