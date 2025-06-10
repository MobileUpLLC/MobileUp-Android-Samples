package ru.mobileup.samples.features.bluetooth.domain.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.mobileup.samples.core.utils.getParcelableExt

fun bluetoothScannerReceiver(
    onAddScanningDevice: (BluetoothDevice) -> Unit
) = object : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice? = intent.getParcelableExt(BluetoothDevice.EXTRA_DEVICE)
                device?.let { onAddScanningDevice(device) }
            }
        }
    }
}