package ru.mobileup.samples.features.qr_code.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.qr_code.createQrCodeGeneratorComponent
import ru.mobileup.samples.features.qr_code.createQrCodeMainComponent
import ru.mobileup.samples.features.qr_code.createQrCodeScannerComponent
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainComponent

class RealQrCodeComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, QrCodeComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: StateFlow<ChildStack<*, QrCodeComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Main,
            serializer = Config.serializer(),
            childFactory = ::createChild,
            handleBackButton = true
        ).toStateFlow(lifecycle)

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ) = when (config) {
        Config.Generator -> QrCodeComponent.Child.Generator(
            componentFactory.createQrCodeGeneratorComponent(componentContext)
        )

        Config.Main -> QrCodeComponent.Child.Main(
            componentFactory.createQrCodeMainComponent(componentContext, ::onMainOutput)
        )

        Config.Scanner -> QrCodeComponent.Child.Scanner(
            componentFactory.createQrCodeScannerComponent(componentContext)
        )
    }

    private fun onMainOutput(output: QrCodeMainComponent.Output) {
        when (output) {
            QrCodeMainComponent.Output.GeneratorRequested -> navigation.safePush(Config.Generator)
            QrCodeMainComponent.Output.ScannerRequested -> navigation.safePush(Config.Scanner)
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Generator : Config

        @Serializable
        data object Scanner : Config

        @Serializable
        data object Main : Config
    }
}