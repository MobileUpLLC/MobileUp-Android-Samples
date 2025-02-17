package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import com.arkivanov.decompose.ComponentContext

class RealCollapsingToolbarExampleComponent(
    componentContext: ComponentContext,
    override val isCommon: Boolean
) : CollapsingToolbarExampleComponent, ComponentContext by componentContext