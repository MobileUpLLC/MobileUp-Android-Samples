package ru.mobileup.samples.features.divkit

import com.arkivanov.decompose.ComponentContext
import com.fasterxml.jackson.databind.json.JsonMapper
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.divkit.data.api.DivKitApi
import ru.mobileup.samples.features.divkit.data.api.DivKitApiImpl
import ru.mobileup.samples.features.divkit.data.db.DivKitGoodsDb
import ru.mobileup.samples.features.divkit.data.db.DivKitGoodsDbImpl
import ru.mobileup.samples.features.divkit.data.DivKitRepository
import ru.mobileup.samples.features.divkit.data.DivKitRepositoryImpl
import ru.mobileup.samples.features.divkit.presentation.DivKitComponent
import ru.mobileup.samples.features.divkit.presentation.RealDivKitComponent
import ru.mobileup.samples.features.divkit.presentation.example_details.DivKitExampleDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.example_details.RealDivKitExampleDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.examples_list.DivKitExamplesListComponent
import ru.mobileup.samples.features.divkit.presentation.examples_list.RealDivKitExamplesListComponent
import ru.mobileup.samples.features.divkit.presentation.goods.details.DivKitProductDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.goods.details.RealDivKitProductDetailsComponent
import ru.mobileup.samples.features.divkit.presentation.goods.list.DivKitGoodsListComponent
import ru.mobileup.samples.features.divkit.presentation.goods.list.RealDivKitGoodsListComponent

val divKitModule = module {
    single<DivParsingEnvironment> { DivParsingEnvironment(ParsingErrorLogger.LOG) }
    single<DivKitRepository> { DivKitRepositoryImpl(get(), get(), get()) }
    single<DivKitApi> { DivKitApiImpl(get(), get()) }
    single<DivKitGoodsDb> { DivKitGoodsDbImpl() }
    single { JsonMapper.builder().build() }
}

fun ComponentFactory.createDivKitComponent(componentContext: ComponentContext): DivKitComponent =
    RealDivKitComponent(componentContext, get())

fun ComponentFactory.createDivKitExamplesListComponent(
    componentContext: ComponentContext,
    output: (DivKitExamplesListComponent.Output) -> Unit
): DivKitExamplesListComponent = RealDivKitExamplesListComponent(
    componentContext,
    get(),
    output,
    get(),
    get()
)

fun ComponentFactory.createDivKitExampleDetailsComponent(
    componentContext: ComponentContext,
    title: String,
    jsonName: String
): DivKitExampleDetailsComponent = RealDivKitExampleDetailsComponent(
    componentContext,
    get(),
    title,
    jsonName,
    get(),
    get(),
    get(),
)

fun ComponentFactory.createDivKitGoodsListComponent(
    componentContext: ComponentContext,
    onOutput: (DivKitGoodsListComponent.Output) -> Unit
): DivKitGoodsListComponent = RealDivKitGoodsListComponent(
    componentContext,
    onOutput,
    get(),
    get(),
    get(),
)

fun ComponentFactory.createDivKitProductDetailsComponent(
    componentContext: ComponentContext,
    id: Long,
    onBackClick: () -> Unit
): DivKitProductDetailsComponent = RealDivKitProductDetailsComponent(
    componentContext,
    id,
    get(),
    get(),
    get(),
    onBackClick
)