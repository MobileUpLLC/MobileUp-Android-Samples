package ru.mobileup.samples.features.divkit.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.divkit.presentation.example_details.DivKitExampleDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.examples_list.DivKitExamplesListComponent
import ru.mobileup.samples.features.divkit.presentation.goods.details.DivKitProductDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.goods.list.DivKitGoodsListComponent

interface DivKitComponent : PredictiveBackComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class ExamplesList(val component: DivKitExamplesListComponent) : Child
        class ExampleDetails(val component: DivKitExampleDetailsComponent) : Child
        class GoodsList(val component: DivKitGoodsListComponent) : Child
        class ProductDetails(val component: DivKitProductDetailsComponent) : Child
    }
}
