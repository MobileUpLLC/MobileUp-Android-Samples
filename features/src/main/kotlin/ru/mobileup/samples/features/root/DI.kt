package ru.mobileup.samples.features.root

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.root.presentation.RealRootComponent
import ru.mobileup.samples.features.root.presentation.RootComponent

fun ComponentFactory.createRootComponent(componentContext: ComponentContext): RootComponent {
    return RealRootComponent(componentContext, get(), get())
}