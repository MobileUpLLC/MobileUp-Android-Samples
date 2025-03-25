package ru.mobileup.samples.features.yandex_map

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.yandex_map.data.MapRepository
import ru.mobileup.samples.features.yandex_map.data.MapRepositoryImpl
import ru.mobileup.samples.features.yandex_map.presentation.RealYandexMapComponent
import ru.mobileup.samples.features.yandex_map.presentation.YandexMapComponent

val yandexMapModule = module {
    single<MapRepository> { MapRepositoryImpl(get()) }
}

fun ComponentFactory.createYandexMapComponent(
    componentContext: ComponentContext
): YandexMapComponent {
    return RealYandexMapComponent(
        componentContext,
        get(),
        get(),
        get(),
        get(),
        get(),
    )
}