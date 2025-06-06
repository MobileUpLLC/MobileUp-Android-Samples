package ru.mobileup.samples.features.bluetooth.domain.events

interface BluetoothScanningState {
    data object None : BluetoothScanningState
    data object InProgress : BluetoothScanningState
    data object NonPermission : BluetoothScanningState
    data object Error : BluetoothScanningState
}

fun BluetoothScanningState.inProgress() = this is BluetoothScanningState.InProgress

fun BluetoothScanningState.nonPermissions() = this is BluetoothScanningState.NonPermission

fun BluetoothScanningState.inError() = this is BluetoothScanningState.Error