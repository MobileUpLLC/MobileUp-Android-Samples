package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

class RealCollapsingToolbarExampleComponent(
    componentContext: ComponentContext,
    override val isLazyLayout: Boolean,
    override val type: CollapsingToolbarLayoutType,
) : CollapsingToolbarExampleComponent, ComponentContext by componentContext