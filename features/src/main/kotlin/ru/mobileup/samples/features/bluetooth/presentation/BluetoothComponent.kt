package ru.mobileup.samples.features.bluetooth.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.bluetooth.presentation.devices.BluetoothDevicesComponent
import ru.mobileup.samples.features.bluetooth.presentation.menu.BluetoothMenuComponent

interface BluetoothComponent : PredictiveBackComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Menu(val component: BluetoothMenuComponent) : Child
        class Devices(val component: BluetoothDevicesComponent) : Child
    }
}