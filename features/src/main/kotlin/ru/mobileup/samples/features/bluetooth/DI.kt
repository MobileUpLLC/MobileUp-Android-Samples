package ru.mobileup.samples.features.bluetooth

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.bluetooth.domain.BluetoothService
import ru.mobileup.samples.features.bluetooth.presentation.BluetoothComponent
import ru.mobileup.samples.features.bluetooth.presentation.RealBluetoothComponent
import ru.mobileup.samples.features.bluetooth.presentation.devices.BluetoothDevicesComponent
import ru.mobileup.samples.features.bluetooth.presentation.devices.RealBluetoothDevicesComponent
import ru.mobileup.samples.features.bluetooth.presentation.menu.BluetoothMenuComponent
import ru.mobileup.samples.features.bluetooth.presentation.menu.RealBluetoothMenuComponent

val bluetoothModule = module {
    factory { BluetoothService(get(), get()) }
}

fun ComponentFactory.createBluetoothComponent(
    componentContext: ComponentContext,
): BluetoothComponent = RealBluetoothComponent(componentContext, get())

fun ComponentFactory.createBluetoothMenuComponent(
    componentContext: ComponentContext,
    onOutput: (BluetoothMenuComponent.Output) -> Unit
): BluetoothMenuComponent = RealBluetoothMenuComponent(
    componentContext,
    onOutput
)

fun ComponentFactory.createBluetoothDevicesComponent(
    componentContext: ComponentContext
): BluetoothDevicesComponent = RealBluetoothDevicesComponent(componentContext, get(), get(), get())