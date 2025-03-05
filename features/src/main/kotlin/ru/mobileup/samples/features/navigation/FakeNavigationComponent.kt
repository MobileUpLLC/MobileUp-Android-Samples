package ru.mobileup.samples.features.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStack
import ru.mobileup.samples.features.navigation.NavigationComponent.Child
import ru.mobileup.samples.features.navigation.NavigationComponent.Tab
import ru.mobileup.samples.features.navigation.nested.FakeNavigationNestedComponent

class FakeNavigationComponent : NavigationComponent {
    override val stack: StateFlow<ChildStack<*, Child>> =
        MutableStateFlow(
            createFakeChildStack(
                Child.Nested(
                    FakeNavigationNestedComponent()
                )
            )
        )

    override val selectedTab: StateFlow<Tab> = MutableStateFlow(Tab.Nested)

    override val isBottomBarVisible: StateFlow<Boolean> = MutableStateFlow(true)

    override fun onTabSelect(tab: Tab): Unit = Unit

    override fun onBottomBarVisibilityChange(isVisible: Boolean): Unit = Unit
}
