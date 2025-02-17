package ru.mobileup.samples.features.collapsing_toolbar.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.collapsing_toolbar.createCollapsingToolbarExampleComponent
import ru.mobileup.samples.features.collapsing_toolbar.createCollapsingToolbarMenuComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.CollapsingToolbarMenuComponent

class RealCollapsingToolbarComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : CollapsingToolbarComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): CollapsingToolbarComponent.Child = when (config) {
        ChildConfig.Menu -> CollapsingToolbarComponent.Child.Menu(
            componentFactory.createCollapsingToolbarMenuComponent(componentContext, ::onMenuOutput)
        )
        is ChildConfig.Example -> CollapsingToolbarComponent.Child.Example(
            componentFactory.createCollapsingToolbarExampleComponent(
                componentContext,
                config.isCommon
            )
        )
    }

    private fun onMenuOutput(output: CollapsingToolbarMenuComponent.Output) {
        when (output) {
            is CollapsingToolbarMenuComponent.Output.ExampleRequested ->
                navigation.safePush(ChildConfig.Example(output.isCommon))
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data class Example(val isCommon: Boolean) : ChildConfig
    }
}