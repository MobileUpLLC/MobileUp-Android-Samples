package ru.mobileup.samples.features.pin_code.presentation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.pin_code.createCreatePinCodeComponent
import ru.mobileup.samples.features.pin_code.createPinCodeSettingsMainComponent
import ru.mobileup.samples.features.pin_code.presentation.create.CreatePinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.settings_main.PinCodeSettingsMainComponent

class RealPinCodeSettingsComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, PinCodeSettingsComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, PinCodeSettingsComponent.Child>> = childStack(
        source = navigation,
        serializer = ChildConfig.serializer(),
        initialConfiguration = ChildConfig.Main,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        childConfig: ChildConfig,
        componentContext: ComponentContext
    ): PinCodeSettingsComponent.Child = when (childConfig) {
        ChildConfig.Create -> PinCodeSettingsComponent.Child.Create(
            componentFactory.createCreatePinCodeComponent(
                componentContext,
                ::onCreatePinCodeOutput
            )
        )
        ChildConfig.Main -> PinCodeSettingsComponent.Child.Main(
            componentFactory.createPinCodeSettingsMainComponent(
                componentContext, ::onMainOutput
            )
        )
    }

    private fun onMainOutput(output: PinCodeSettingsMainComponent.Output) {
        when (output) {
            PinCodeSettingsMainComponent.Output.CreatePinCodeRequested -> navigation.safePush(ChildConfig.Create)
        }
    }

    private fun onCreatePinCodeOutput(output: CreatePinCodeComponent.Output) {
        when (output) {
            CreatePinCodeComponent.Output.PinCodeSet -> navigation.pop()
        }
    }

    @Serializable
    sealed interface ChildConfig {
        @Serializable
        data object Main : ChildConfig

        @Serializable
        data object Create : ChildConfig
    }
}