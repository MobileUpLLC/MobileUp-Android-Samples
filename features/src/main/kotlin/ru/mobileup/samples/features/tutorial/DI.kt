package ru.mobileup.samples.features.tutorial

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.features.tutorial.presentation.sample.RealTutorialSampleComponent
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.tutorial.data.TutorialStatusStorage
import ru.mobileup.samples.features.tutorial.data.TutorialStatusStorageImpl
import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.tutorial.domain.TutorialManagerImpl
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.RealTutorialManagementSampleComponent

val tutorialModule = module {
    single<TutorialStatusStorage> { TutorialStatusStorageImpl(get()) }
    single<TutorialManager> { TutorialManagerImpl() }
}

fun ComponentFactory.createTutorialSampleComponent(
    componentContext: ComponentContext
) = RealTutorialSampleComponent(componentContext, get())

fun ComponentFactory.createTutorialManagementSampleComponent(
    componentContext: ComponentContext
) = RealTutorialManagementSampleComponent(componentContext, get(), get(), get())