package ru.mobileup.samples.features.bluetooth.domain

sealed interface BluetoothServiceState {
    data object NotSupported : BluetoothServiceState
    data object BluetoothOff : BluetoothServiceState
    data object BluetoothOn : BluetoothServiceState
}
