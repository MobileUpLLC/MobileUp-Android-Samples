package ru.mobileup.samples.features.multipane_menu.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
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
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.navigation.createNavigationComponent
import ru.mobileup.samples.features.otp.createOtpComponent
import ru.mobileup.samples.features.otp.presentation.OtpComponent
import ru.mobileup.samples.features.photo.createPhotoComponent
import ru.mobileup.samples.features.pin_code.createPinCodeSettingsComponent
import ru.mobileup.samples.features.qr_code.createQrCodeComponent
import ru.mobileup.samples.features.remote_transfer.createRemoteTransferComponent
import ru.mobileup.samples.features.shared_element_transitions.createSharedElementsComponent
import ru.mobileup.samples.features.tutorial.createTutorialSampleComponent
import ru.mobileup.samples.features.video.createVideoComponent
import ru.mobileup.samples.features.work_manager.createWorkManagerComponent

class RealMultiPaneDetailsComponent(
    componentContext: ComponentContext,
    sample: Sample,
    private val onOutput: (MultiPaneDetailsComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, MultiPaneDetailsComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, MultiPaneDetailsComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = when (sample) {
            Sample.Form -> ChildConfig.Form
            Sample.Otp -> ChildConfig.Otp
            Sample.Photo -> ChildConfig.Photo
            Sample.Video -> ChildConfig.Video
            Sample.Document -> ChildConfig.Document
            Sample.RemoteTransfer -> ChildConfig.Uploader
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
            else -> error("Not supported sample")
        },
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext,
    ): MultiPaneDetailsComponent.Child = when (config) {
        ChildConfig.Form -> MultiPaneDetailsComponent.Child.Form(
            componentFactory.createFormComponent(componentContext)
        )

        ChildConfig.Otp -> MultiPaneDetailsComponent.Child.Otp(
            componentFactory.createOtpComponent(componentContext, ::onOtpOutput)
        )

        ChildConfig.Photo -> MultiPaneDetailsComponent.Child.Photo(
            componentFactory.createPhotoComponent(componentContext)
        )

        ChildConfig.Video -> MultiPaneDetailsComponent.Child.Video(
            componentFactory.createVideoComponent(componentContext)
        )

        ChildConfig.Document -> MultiPaneDetailsComponent.Child.Document(
            componentFactory.createDocumentComponent(componentContext)
        )

        ChildConfig.Uploader -> MultiPaneDetailsComponent.Child.RemoteTransfer(
            componentFactory.createRemoteTransferComponent(componentContext)
        )

        ChildConfig.Calendar -> MultiPaneDetailsComponent.Child.Calendar(
            componentFactory.createCalendarComponent(componentContext)
        )

        ChildConfig.QrCode -> MultiPaneDetailsComponent.Child.QrCode(
            componentFactory.createQrCodeComponent(componentContext)
        )

        ChildConfig.Chart -> MultiPaneDetailsComponent.Child.Chart(
            componentFactory.createChartComponent(componentContext)
        )

        ChildConfig.Navigation -> MultiPaneDetailsComponent.Child.Navigation(
            componentFactory.createNavigationComponent(componentContext)
        )

        ChildConfig.CollapsingToolbar -> MultiPaneDetailsComponent.Child.CollapsingToolbar(
            componentFactory.createCollapsingToolbarComponent(componentContext)
        )

        ChildConfig.Image -> MultiPaneDetailsComponent.Child.Image(
            componentFactory.createImageComponent(componentContext)
        )

        ChildConfig.Tutorial -> MultiPaneDetailsComponent.Child.Tutorial(
            componentFactory.createTutorialSampleComponent(componentContext)
        )

        ChildConfig.SharedElements -> MultiPaneDetailsComponent.Child.SharedElements(
            componentFactory.createSharedElementsComponent(componentContext)
        )

        ChildConfig.PinCodeSettings -> MultiPaneDetailsComponent.Child.PinCodeSettings(
            componentFactory.createPinCodeSettingsComponent(componentContext)
        )

        ChildConfig.Map -> MultiPaneDetailsComponent.Child.Map(
            componentFactory.createMapMainComponent(componentContext)
        )

        ChildConfig.Chat -> MultiPaneDetailsComponent.Child.Chat(
            componentFactory.createChatComponent(componentContext)
        )

        ChildConfig.WorkManager -> MultiPaneDetailsComponent.Child.WorkManager(
            componentFactory.createWorkManagerComponent(componentContext)
        )

        ChildConfig.DivKit -> MultiPaneDetailsComponent.Child.DivKit(
            componentFactory.createDivKitComponent(componentContext)
        )
    }

    private fun onOtpOutput(output: OtpComponent.Output) = when (output) {
        OtpComponent.Output.OtpSuccessfullyVerified -> onOutput(
            MultiPaneDetailsComponent.Output.OtpSuccessfullyVerified
        )
    }

    @Serializable
    sealed interface ChildConfig {

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
        data object WorkManager : ChildConfig

        @Serializable
        data object DivKit : ChildConfig
    }
}
