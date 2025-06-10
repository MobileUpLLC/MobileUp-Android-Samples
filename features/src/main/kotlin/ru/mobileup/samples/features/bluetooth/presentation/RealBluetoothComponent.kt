package ru.mobileup.samples.features.bluetooth.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.bluetooth.createBluetoothDevicesComponent
import ru.mobileup.samples.features.bluetooth.createBluetoothMenuComponent
import ru.mobileup.samples.features.bluetooth.presentation.menu.BluetoothMenuComponent

class RealBluetoothComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, BluetoothComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override fun onBackClick() {
        navigation.pop()
    }

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): BluetoothComponent.Child = when (config) {
        ChildConfig.Menu -> BluetoothComponent.Child.Menu(
            componentFactory.createBluetoothMenuComponent(componentContext, ::onBluetoothMenuOutput)
        )

        ChildConfig.Devices -> BluetoothComponent.Child.Devices(
            componentFactory.createBluetoothDevicesComponent(componentContext)
        )
    }

    private fun onBluetoothMenuOutput(output: BluetoothMenuComponent.Output) {
        when (output) {
            is BluetoothMenuComponent.Output.BluetoothMenuRequest ->
                navigation.safePush(ChildConfig.Devices)
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Devices : ChildConfig
    }
}