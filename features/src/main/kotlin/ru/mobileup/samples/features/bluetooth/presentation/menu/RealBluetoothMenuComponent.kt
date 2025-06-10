package ru.mobileup.samples.features.bluetooth.presentation.menu

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.bluetooth.domain.BluetoothMenu

class RealBluetoothMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (BluetoothMenuComponent.Output) -> Unit
) : ComponentContext by componentContext, BluetoothMenuComponent {

    override fun onMenuSelect(item: BluetoothMenu) {
        onOutput(BluetoothMenuComponent.Output.BluetoothMenuRequest(item))
    }
}