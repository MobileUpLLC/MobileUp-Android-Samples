package ru.mobileup.samples.features.navigation.nested

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.navigation.createNavigationNestedLeafComponent
import ru.mobileup.samples.features.navigation.createNavigationNestedMainComponent
import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainComponent

class RealNavigationNestedComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, NavigationNestedComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: StateFlow<ChildStack<*, NavigationNestedComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Main,
            serializer = Config.serializer(),
            handleBackButton = true,
            childFactory = ::createChild
        ).toStateFlow(lifecycle)

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ) = when (config) {
        Config.LeafWithBottomBar -> NavigationNestedComponent.Child.LeafWithBottomBar(
            componentFactory.createNavigationNestedLeafComponent(
                name = R.string.navigation_nested_with_bottom_bar.strResDesc()
            )
        )

        Config.LeafWithoutBottomBar -> NavigationNestedComponent.Child.LeafWithoutBottomBar(
            componentFactory.createNavigationNestedLeafComponent(
                name = R.string.navigation_nested_without_bottom_bar.strResDesc()
            )
        )

        Config.Main -> NavigationNestedComponent.Child.Main(
            componentFactory.createNavigationNestedMainComponent(
                onOutput = ::onMainOutput
            )
        )
    }

    private fun onMainOutput(output: NavigationNestedMainComponent.Output) {
        when (output) {
            NavigationNestedMainComponent.Output.LeafWithBottomBarRequested -> {
                navigation.safePush(Config.LeafWithBottomBar)
            }

            NavigationNestedMainComponent.Output.LeafWithoutBottomBarRequested -> {
                navigation.safePush(Config.LeafWithoutBottomBar)
            }
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data object LeafWithBottomBar : Config

        @Serializable
        data object LeafWithoutBottomBar : Config
    }
}