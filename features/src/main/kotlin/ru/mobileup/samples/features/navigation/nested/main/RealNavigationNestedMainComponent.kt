package ru.mobileup.samples.features.navigation.nested.main

import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainComponent.Output.LeafWithBottomBarRequested
import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainComponent.Output.LeafWithoutBottomBarRequested

class RealNavigationNestedMainComponent(
    private val onOutput: (NavigationNestedMainComponent.Output) -> Unit
) : NavigationNestedMainComponent {

    override fun onLeafWithBottomBarClick() = onOutput(LeafWithBottomBarRequested)

    override fun onLeafWithoutBottomBarClick() = onOutput(LeafWithoutBottomBarRequested)
}