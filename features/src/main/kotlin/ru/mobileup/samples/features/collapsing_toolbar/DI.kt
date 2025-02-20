package ru.mobileup.samples.features.collapsing_toolbar

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType
import ru.mobileup.samples.features.collapsing_toolbar.presentation.CollapsingToolbarComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.RealCollapsingToolbarComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.CollapsingToolbarMenuComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.RealCollapsingToolbarMenuComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.example.CollapsingToolbarExampleComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.example.RealCollapsingToolbarExampleComponent

fun ComponentFactory.createCollapsingToolbarMenuComponent(
    componentContext: ComponentContext,
    output: (CollapsingToolbarMenuComponent.Output) -> Unit
): CollapsingToolbarMenuComponent = RealCollapsingToolbarMenuComponent(
    componentContext,
    output
)

fun ComponentFactory.createCollapsingToolbarComponent(
    componentContext: ComponentContext,
): CollapsingToolbarComponent = RealCollapsingToolbarComponent(
    componentContext,
    get()
)

fun ComponentFactory.createCollapsingToolbarExampleComponent(
    componentContext: ComponentContext,
    isLazyLayout: Boolean,
    type: CollapsingToolbarLayoutType
): CollapsingToolbarExampleComponent = RealCollapsingToolbarExampleComponent(
    componentContext,
    isLazyLayout,
    type,
)