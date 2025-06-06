package ru.mobileup.samples.features.bluetooth.domain.receivers

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

fun bluetoothStateReceiver(
    isConnected: (Boolean) -> Unit
) = object : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            isConnected(state == BluetoothAdapter.STATE_ON)
        }
    }
}