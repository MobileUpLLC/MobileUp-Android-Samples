package ru.mobileup.samples.features.root.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.createMessageComponent
import ru.mobileup.samples.core.createThemeComponent
import ru.mobileup.samples.core.createTutorialOverlayComponent
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.calendar.createCalendarComponent
import ru.mobileup.samples.features.charts.createChartComponent
import ru.mobileup.samples.features.chat.createChatComponent
import ru.mobileup.samples.features.collapsing_toolbar.createCollapsingToolbarComponent
import ru.mobileup.samples.features.divkit.createDivKitComponent
import ru.mobileup.samples.features.document.createDocumentComponent
import ru.mobileup.samples.features.form.createFormComponent
import ru.mobileup.samples.features.image.createImageComponent
import ru.mobileup.samples.features.map.createMapMainComponent
import ru.mobileup.samples.features.menu.createMenuComponent
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.menu.presentation.MenuComponent
import ru.mobileup.samples.features.navigation.createNavigationComponent
import ru.mobileup.samples.features.otp.createOtpComponent
import ru.mobileup.samples.features.otp.presentation.OtpComponent
import ru.mobileup.samples.features.photo.createPhotoComponent
import ru.mobileup.samples.features.pin_code.createCheckPinCodeManagementComponent
import ru.mobileup.samples.features.pin_code.createPinCodeSettingsComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.CheckPinCodeManagementComponent
import ru.mobileup.samples.features.qr_code.createQrCodeComponent
import ru.mobileup.samples.features.settings.createSettingsComponent
import ru.mobileup.samples.features.shared_element_transitions.createSharedElementsComponent
import ru.mobileup.samples.features.tutorial.createTutorialSampleComponent
import ru.mobileup.samples.features.uploader.createUploaderComponent
import ru.mobileup.samples.features.video.createVideoComponent
import ru.mobileup.samples.features.work_manager.createWorkManagerComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, RootComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override val messageComponent = componentFactory.createMessageComponent(
        childContext("message")
    )

    override val tutorialOverlayComponent = componentFactory.createTutorialOverlayComponent(
        childContext("tutorialOverlay")
    )

    override val checkPinCodeManagementComponent: CheckPinCodeManagementComponent =
        componentFactory.createCheckPinCodeManagementComponent(
            childContext("checkPinCodeManagement")
        )

    override val themeComponent = componentFactory.createThemeComponent(
        childContext("theme")
    )

    override fun onBackClick() = navigation.pop()

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        ChildConfig.Menu -> {
            RootComponent.Child.Menu(
                componentFactory.createMenuComponent(componentContext, ::onMenuOutput)
            )
        }

        ChildConfig.Form -> {
            RootComponent.Child.Form(
                componentFactory.createFormComponent(componentContext)
            )
        }

        ChildConfig.Otp -> {
            RootComponent.Child.Otp(
                componentFactory.createOtpComponent(componentContext, ::onOtpOutput)
            )
        }

        ChildConfig.Photo -> {
            RootComponent.Child.Photo(
                componentFactory.createPhotoComponent(componentContext)
            )
        }

        ChildConfig.Video -> {
            RootComponent.Child.Video(
                componentFactory.createVideoComponent(componentContext)
            )
        }

        ChildConfig.Document -> {
            RootComponent.Child.Document(
                componentFactory.createDocumentComponent(componentContext)
            )
        }

        ChildConfig.Uploader -> {
            RootComponent.Child.Uploader(
                componentFactory.createUploaderComponent(componentContext)
            )
        }

        ChildConfig.Calendar -> {
            RootComponent.Child.Calendar(
                componentFactory.createCalendarComponent(componentContext)
            )
        }

        ChildConfig.QrCode -> {
            RootComponent.Child.QrCode(
                componentFactory.createQrCodeComponent(componentContext)
            )
        }

        ChildConfig.Chart -> {
            RootComponent.Child.Chart(
                componentFactory.createChartComponent(componentContext)
            )
        }

        ChildConfig.Navigation -> {
            RootComponent.Child.Navigation(
                componentFactory.createNavigationComponent(componentContext)
            )
        }

        ChildConfig.CollapsingToolbar -> {
            RootComponent.Child.CollapsingToolbar(
                componentFactory.createCollapsingToolbarComponent(componentContext)
            )
        }

        ChildConfig.Image -> {
            RootComponent.Child.Image(
                componentFactory.createImageComponent(componentContext)
            )
        }

        ChildConfig.Tutorial -> {
            RootComponent.Child.Tutorial(
                componentFactory.createTutorialSampleComponent(componentContext)
            )
        }

        ChildConfig.SharedElements -> {
            RootComponent.Child.SharedElements(
                componentFactory.createSharedElementsComponent(componentContext)
            )
        }

        ChildConfig.PinCodeSettings -> {
            RootComponent.Child.PinCodeSettings(
                componentFactory.createPinCodeSettingsComponent(componentContext)
            )
        }

        ChildConfig.Map -> {
            RootComponent.Child.Map(
                componentFactory.createMapMainComponent(componentContext)
            )
        }

        ChildConfig.Settings -> {
            RootComponent.Child.Settings(
                componentFactory.createSettingsComponent(componentContext)
            )
        }

        ChildConfig.Chat -> {
            RootComponent.Child.Chat(
                componentFactory.createChatComponent(componentContext)
            )
        }

        ChildConfig.WorkManager -> {
            RootComponent.Child.WorkManager(
                componentFactory.createWorkManagerComponent(componentContext)
            )
        }

        ChildConfig.DivKit -> RootComponent.Child.DivKit(
            componentFactory.createDivKitComponent(componentContext)
        )
    }

    private fun onMenuOutput(output: MenuComponent.Output) {
        when (output) {
            is MenuComponent.Output.SampleChosen -> when (output.sample) {
                Sample.Form -> ChildConfig.Form
                Sample.Otp -> ChildConfig.Otp
                Sample.Photo -> ChildConfig.Photo
                Sample.Video -> ChildConfig.Video
                Sample.Document -> ChildConfig.Document
                Sample.Uploader -> ChildConfig.Uploader
                Sample.Calendar -> ChildConfig.Calendar
                Sample.QrCode -> ChildConfig.QrCode
                Sample.Chart -> ChildConfig.Chart
                Sample.Navigation -> ChildConfig.Navigation
                Sample.CollapsingToolbar -> ChildConfig.CollapsingToolbar
                Sample.Image -> ChildConfig.Image
                Sample.Tutorial -> ChildConfig.Tutorial
                Sample.SharedTransitions -> ChildConfig.SharedElements
                Sample.PinCodeSettings -> ChildConfig.PinCodeSettings
                Sample.Map -> ChildConfig.Map
                Sample.Chat -> ChildConfig.Chat
                Sample.WorkManager -> ChildConfig.WorkManager
                Sample.DivKit -> ChildConfig.DivKit
            }.run(navigation::safePush)

            MenuComponent.Output.SettingsRequested -> navigation.safePush(ChildConfig.Settings)
        }
    }

    private fun onOtpOutput(output: OtpComponent.Output) {
        when (output) {
            OtpComponent.Output.OtpSuccessfullyVerified -> navigation.pop()
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Form : ChildConfig

        @Serializable
        data object Otp : ChildConfig

        @Serializable
        data object Photo : ChildConfig

        @Serializable
        data object Video : ChildConfig

        @Serializable
        data object Document : ChildConfig

        @Serializable
        data object Uploader : ChildConfig

        @Serializable
        data object Calendar : ChildConfig

        @Serializable
        data object QrCode : ChildConfig

        @Serializable
        data object Chart : ChildConfig

        @Serializable
        data object Navigation : ChildConfig

        @Serializable
        data object CollapsingToolbar : ChildConfig

        @Serializable
        data object Image : ChildConfig

        @Serializable
        data object Tutorial : ChildConfig

        @Serializable
        data object SharedElements : ChildConfig

        @Serializable
        data object PinCodeSettings : ChildConfig

        @Serializable
        data object Map : ChildConfig

        @Serializable
        data object Chat : ChildConfig

        @Serializable
        data object Settings : ChildConfig

        @Serializable
        data object WorkManager : ChildConfig

        @Serializable
        data object DivKit : ChildConfig
    }
}
