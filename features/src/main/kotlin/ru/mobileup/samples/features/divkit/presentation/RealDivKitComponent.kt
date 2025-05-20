package ru.mobileup.samples.features.divkit.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.divkit.createDivKitExampleDetailsComponent
import ru.mobileup.samples.features.divkit.createDivKitExamplesListComponent
import ru.mobileup.samples.features.divkit.createDivKitGoodsListComponent
import ru.mobileup.samples.features.divkit.createDivKitProductDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.examples_list.DivKitExamplesListComponent
import ru.mobileup.samples.features.divkit.presentation.goods.list.DivKitGoodsListComponent

class RealDivKitComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, DivKitComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.ExamplesList,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): DivKitComponent.Child = when (config) {
        is ChildConfig.ExampleDetails -> DivKitComponent.Child.ExampleDetails(
            componentFactory.createDivKitExampleDetailsComponent(
                componentContext,
                config.title,
                config.jsonName
            )
        )
        ChildConfig.ExamplesList -> DivKitComponent.Child.ExamplesList(
            componentFactory.createDivKitExamplesListComponent(componentContext, ::onDivKitListOutput)
        )

        ChildConfig.GoodsList -> DivKitComponent.Child.GoodsList(
            componentFactory.createDivKitGoodsListComponent(componentContext, ::onDivKitGoodsListOutput)
        )

        is ChildConfig.ProductDetails -> DivKitComponent.Child.ProductDetails(
            componentFactory.createDivKitProductDetailsComponent(
                componentContext,
                config.id,
                onBackClick = { navigation.pop() }
            )
        )
    }

    private fun onDivKitListOutput(output: DivKitExamplesListComponent.Output) {
        when (output) {
            is DivKitExamplesListComponent.Output.DetailsRequested -> {
                if (output.jsonName == DivKitExamplesListComponent.Action.GoodList.jsonName) {
                    navigation.safePush(ChildConfig.GoodsList)
                } else {
                    navigation.safePush(ChildConfig.ExampleDetails(output.title, output.jsonName))
                }
            }
        }
    }

    private fun onDivKitGoodsListOutput(output: DivKitGoodsListComponent.Output) {
        when (output) {
            is DivKitGoodsListComponent.Output.DetailsRequested ->
                navigation.safePush(ChildConfig.ProductDetails(output.id))
        }
    }

    override fun onBackClick() {
        navigation.pop()
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object ExamplesList : ChildConfig

        @Serializable
        data class ExampleDetails(
            val title: String,
            val jsonName: String
        ) : ChildConfig

        @Serializable
        data object GoodsList : ChildConfig

        @Serializable
        data class ProductDetails(val id: Long) : ChildConfig
    }
}