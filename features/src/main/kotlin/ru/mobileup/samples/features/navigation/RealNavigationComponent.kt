package ru.mobileup.samples.features.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.navigation.NavigationComponent.Tab

class RealNavigationComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, NavigationComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: StateFlow<ChildStack<*, NavigationComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Nested,
        serializer = Config.serializer(),
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ) = when (config) {
        Config.AlertDialogs -> NavigationComponent.Child.AlertDialogs(
            componentFactory.createNavigationAlertDialogsComponent(componentContext)
        )

        Config.BottomSheets -> NavigationComponent.Child.BottomSheets(
            componentFactory.createNavigationBottomSheetsComponent(componentContext)
        )

        Config.Nested -> NavigationComponent.Child.Nested(
            componentFactory.createNavigationNestedComponent(componentContext)
        )
    }

    override val selectedTab = computed(stack) {
        when (it.active.instance) {
            is NavigationComponent.Child.AlertDialogs -> Tab.AlertDialogs
            is NavigationComponent.Child.BottomSheets -> Tab.BottomSheets
            is NavigationComponent.Child.Nested -> Tab.Nested
        }
    }

    override val isBottomBarVisible = MutableStateFlow(true)

    override fun onTabSelect(tab: Tab) = navigation.bringToFront(
        when (tab) {
            Tab.Nested -> Config.Nested
            Tab.BottomSheets -> Config.BottomSheets
            Tab.AlertDialogs -> Config.AlertDialogs
        }
    )

    override fun onBottomBarVisibilityChange(isVisible: Boolean) = isBottomBarVisible.update { isVisible }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Nested : Config

        @Serializable
        data object BottomSheets : Config

        @Serializable
        data object AlertDialogs : Config
    }
}