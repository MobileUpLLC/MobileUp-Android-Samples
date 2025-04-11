package ru.mobileup.samples.core

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.network.AndroidNetworkConnectivityProvider
import me.aartikov.replica.network.NetworkConnectivityProvider
import org.koin.core.component.get
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.app_settings.data.AppSettingsStorage
import ru.mobileup.samples.core.app_settings.data.AppSettingsStorageImpl
import ru.mobileup.samples.core.biometric.data.AndroidBiometricService
import ru.mobileup.samples.core.biometric.data.BiometricEnablingStorage
import ru.mobileup.samples.core.biometric.data.BiometricEnablingStorageImpl
import ru.mobileup.samples.core.biometric.data.BiometricService
import ru.mobileup.samples.core.debug_tools.DebugTools
import ru.mobileup.samples.core.debug_tools.RealDebugTools
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.external_apps.data.ExternalAppService
import ru.mobileup.samples.core.external_apps.data.ExternalAppServiceImpl
import ru.mobileup.samples.core.location.AndroidLocationService
import ru.mobileup.samples.core.location.LocationService
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.data.MessageServiceImpl
import ru.mobileup.samples.core.message.presentation.MessageComponent
import ru.mobileup.samples.core.message.presentation.RealMessageComponent
import ru.mobileup.samples.core.network.NetworkApiFactory
import ru.mobileup.samples.core.network.createOkHttpEngine
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.settings.AndroidSettingsFactory
import ru.mobileup.samples.core.settings.SettingsFactory
import ru.mobileup.samples.core.sharing.data.SharingService
import ru.mobileup.samples.core.sharing.data.SharingServiceImpl
import ru.mobileup.samples.core.theme.component.RealThemeComponent
import ru.mobileup.samples.core.theme.component.ThemeComponent
import ru.mobileup.samples.core.tutorial.data.TutorialStatusStorage
import ru.mobileup.samples.core.tutorial.data.TutorialStatusStorageImpl
import ru.mobileup.samples.core.tutorial.domain.Tutorial
import ru.mobileup.samples.core.tutorial.domain.TutorialManager
import ru.mobileup.samples.core.tutorial.domain.TutorialManagerImpl
import ru.mobileup.samples.core.tutorial.presentation.management.RealTutorialManagementComponent
import ru.mobileup.samples.core.tutorial.presentation.management.TutorialManagementComponent
import ru.mobileup.samples.core.tutorial.presentation.overlay.RealTutorialOverlayComponent
import ru.mobileup.samples.core.tutorial.presentation.overlay.TutorialOverlayComponent

fun coreModule(backendUrl: String) = module {
    single { ActivityProvider() }
    single<NetworkConnectivityProvider> { AndroidNetworkConnectivityProvider(get()) }
    single { ReplicaClient(get()) }
    single<MessageService> { MessageServiceImpl() }
    single { ErrorHandler(get()) }
    single<DebugTools> { RealDebugTools(get(), get()) }
    single { createOkHttpEngine(get()) }
    single {
        NetworkApiFactory(
            loggingEnabled = BuildConfig.DEBUG,
            backendUrl = backendUrl,
            httpClientEngine = get()
        )
    }
    single(createdAtStart = true) { PermissionService(get(), get()) }
    single<SettingsFactory> { AndroidSettingsFactory(get(), Dispatchers.IO) }
    singleOf(::ExternalAppServiceImpl) bind ExternalAppService::class
    single<TutorialStatusStorage> { TutorialStatusStorageImpl(get()) }
    single<TutorialManager> { TutorialManagerImpl() }
    single<BiometricService> { AndroidBiometricService(get(), get()) }
    single<BiometricEnablingStorage> { BiometricEnablingStorageImpl(get()) }
    single<LocationService> { AndroidLocationService(get()) }
    single<AppSettingsStorage> { AppSettingsStorageImpl(get()) }
    single<SharingService> { SharingServiceImpl(get()) }
}

fun ComponentFactory.createMessageComponent(
    componentContext: ComponentContext
): MessageComponent {
    return RealMessageComponent(componentContext, get())
}

fun ComponentFactory.createTutorialManagementComponent(
    componentContext: ComponentContext,
    tutorial: Tutorial
): TutorialManagementComponent {
    return RealTutorialManagementComponent(
        componentContext,
        tutorial,
        get(),
        get(),
        get()
    )
}

fun ComponentFactory.createTutorialOverlayComponent(
    componentContext: ComponentContext
): TutorialOverlayComponent {
    return RealTutorialOverlayComponent(componentContext, get())
}

fun ComponentFactory.createThemeComponent(
    componentContext: ComponentContext,
): ThemeComponent = RealThemeComponent(componentContext, get())
