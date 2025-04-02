package ru.mobileup.samples.features.optimistic_update

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.optimistic_update.data.OptimisticUpdateApi
import ru.mobileup.samples.features.optimistic_update.data.client.OptimisticUpdateClientRepository
import ru.mobileup.samples.features.optimistic_update.data.client.OptimisticUpdateClientRepositoryImpl
import ru.mobileup.samples.features.optimistic_update.data.server.OptimisticUpdateServerRepository
import ru.mobileup.samples.features.optimistic_update.data.server.OptimisticUpdateServerRepositoryImpl
import ru.mobileup.samples.features.optimistic_update.domain.job_launcher.JobLauncherStore
import ru.mobileup.samples.features.optimistic_update.presentation.OptimisticUpdateComponent
import ru.mobileup.samples.features.optimistic_update.presentation.RealOptimisticUpdateComponent
import ru.mobileup.samples.features.optimistic_update.presentation.server.OptimisticUpdateServerComponent
import ru.mobileup.samples.features.optimistic_update.presentation.server.RealOptimisticUpdateServerComponent

val optimisticUpdateModule = module {
    singleOf(::OptimisticUpdateServerRepositoryImpl) binds arrayOf(
        OptimisticUpdateApi::class,
        OptimisticUpdateServerRepository::class
    )
    singleOf(::OptimisticUpdateClientRepositoryImpl) bind OptimisticUpdateClientRepository::class
    singleOf(JobLauncherStore::getOrCreateInstance)
}

fun ComponentFactory.createOptimisticUpdateServerComponent(
    componentContext: ComponentContext
): OptimisticUpdateServerComponent {
    return RealOptimisticUpdateServerComponent(
        componentContext = componentContext,
        get(),
        get(),
    )
}

fun ComponentFactory.createOptimisticUpdateComponent(
    componentContext: ComponentContext
): OptimisticUpdateComponent {
    return RealOptimisticUpdateComponent(
        componentContext = componentContext,
        get(),
        get(),
        get(),
        get(),
    )
}
