package ru.mobileup.samples.features.image

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.image.domain.ImageResource
import ru.mobileup.samples.features.image.presentation.RealImageComponent
import ru.mobileup.samples.features.image.presentation.carousel.RealImageCarouselComponent

fun ComponentFactory.createImageComponent(
    componentContext: ComponentContext
) = RealImageComponent(componentContext, get())

fun ComponentFactory.createImageCarouselComponent(
    imageResources: List<ImageResource>,
    componentContext: ComponentContext
) = RealImageCarouselComponent(imageResources, componentContext)
