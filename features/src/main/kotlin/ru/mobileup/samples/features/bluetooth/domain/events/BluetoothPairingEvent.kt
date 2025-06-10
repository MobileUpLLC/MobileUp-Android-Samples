package ru.mobileup.samples.features.bluetooth.domain.events

sealed interface BluetoothPairingEvent {
    data object DeviceAlreadyPaired : BluetoothPairingEvent
    data object NonPermission : BluetoothPairingEvent
    data object PairingRequested : BluetoothPairingEvent
    data object Error : BluetoothPairingEvent
}