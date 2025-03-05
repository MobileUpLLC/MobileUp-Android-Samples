package ru.mobileup.samples.features.navigation.nested.main

interface NavigationNestedMainComponent {

    fun onLeafWithBottomBarClick()
    fun onLeafWithoutBottomBarClick()

    sealed interface Output {
        data object LeafWithBottomBarRequested : Output
        data object LeafWithoutBottomBarRequested : Output
    }
}