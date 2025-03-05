package ru.mobileup.samples.features.navigation.nested

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.navigation.nested.leaf.NavigationNestedLeafComponent
import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainComponent

interface NavigationNestedComponent {

    val stack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Main(val component: NavigationNestedMainComponent) : Child
        class LeafWithBottomBar(val component: NavigationNestedLeafComponent) : Child
        class LeafWithoutBottomBar(val component: NavigationNestedLeafComponent) : Child
    }
}