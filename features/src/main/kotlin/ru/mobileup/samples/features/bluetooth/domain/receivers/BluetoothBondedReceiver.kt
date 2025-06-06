package ru.mobileup.samples.features.bluetooth.domain.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.mobileup.samples.core.utils.getParcelableExt

fun bluetoothBondedReceiver(
    onAddBoundedDevicesUpdate: (device: BluetoothDevice) -> Unit
) = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
            intent
                .getParcelableExt<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                ?.let(onAddBoundedDevicesUpdate)
        }
    }
}