package ru.mobileup.samples.features.root.presentation

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.createMessageComponent
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.calendar.createCalendarComponent
import ru.mobileup.samples.features.charts.createChartComponent
import ru.mobileup.samples.features.form.createFormComponent
import ru.mobileup.samples.features.menu.createMenuComponent
import ru.mobileup.samples.features.menu.domain.Sample
import ru.mobileup.samples.features.menu.presentation.MenuComponent
import ru.mobileup.samples.features.navigation.createNavigationComponent
import ru.mobileup.samples.features.qr_code.createQrCodeComponent
import ru.mobileup.samples.features.tutorial.createTutorialSampleComponent
import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.video.createVideoComponent

class RealRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    override val tutorialManager: TutorialManager
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
        childContext(key = "message")
    )

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is ChildConfig.Menu -> {
            RootComponent.Child.Menu(
                componentFactory.createMenuComponent(componentContext, ::onMenuOutput)
            )
        }

        is ChildConfig.Form -> {
            RootComponent.Child.Form(
                componentFactory.createFormComponent(componentContext)
            )
        }

        is ChildConfig.Video -> {
            RootComponent.Child.Video(
                componentFactory.createVideoComponent(componentContext)
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

        ChildConfig.Tutorial -> {
            RootComponent.Child.Tutorial(
                componentFactory.createTutorialSampleComponent(componentContext)
            )
        }
    }

    private fun onMenuOutput(output: MenuComponent.Output) {
        when (output) {
            is MenuComponent.Output.SampleChosen -> navigation.safePush(
                when (output.sample) {
                    Sample.Form -> ChildConfig.Form
                    Sample.Video -> ChildConfig.Video
                    Sample.Calendar -> ChildConfig.Calendar
                    Sample.QrCode -> ChildConfig.QrCode
                    Sample.Chart -> ChildConfig.Chart
                    Sample.Navigation -> ChildConfig.Navigation
                    Sample.Tutorial -> ChildConfig.Tutorial
                }
            )
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Form : ChildConfig

        @Serializable
        data object Video : ChildConfig

        @Serializable
        data object Calendar : ChildConfig

        @Serializable
        data object QrCode : ChildConfig

        @Serializable
        data object Chart : ChildConfig

        @Serializable
        data object Navigation : ChildConfig

        @Serializable
        data object Tutorial : ChildConfig
    }
}
