package ru.mobileup.samples.features.bluetooth.presentation.menu

import ru.mobileup.samples.features.bluetooth.domain.BluetoothMenu
import ru.mobileup.samples.features.charts.presentation.menu.ChartMenuComponent.Output

interface BluetoothMenuComponent {

    fun onMenuSelect(item: BluetoothMenu)

    sealed interface Output {
        data class BluetoothMenuRequest(val bluetoothMenu: BluetoothMenu) : Output
    }
}