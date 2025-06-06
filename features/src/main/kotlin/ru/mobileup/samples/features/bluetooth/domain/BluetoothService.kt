package ru.mobileup.samples.features.bluetooth.domain

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.BOND_BONDED
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.permissions.SinglePermissionResult
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothPairingEvent
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothScanningState
import ru.mobileup.samples.features.bluetooth.domain.receivers.bluetoothBondedReceiver
import ru.mobileup.samples.features.bluetooth.domain.receivers.bluetoothScannerReceiver
import ru.mobileup.samples.features.bluetooth.domain.receivers.bluetoothStateReceiver
import ru.mobileup.samples.features.bluetooth.domain.utils.mapToFoundDevice
import java.io.IOException

@SuppressLint("MissingPermission")
class BluetoothService(
    private val context: Context,
    private val permissionService: PermissionService,
) {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _bluetoothScannedDevices = MutableStateFlow(BluetoothScannedDevices.empty)

    val bluetoothScannedDevices = _bluetoothScannedDevices.asStateFlow()

    private val _scanningState = MutableStateFlow<BluetoothScanningState>(BluetoothScanningState.None)
    val scanningState = _scanningState.asStateFlow()

    private val scannerReceiver = bluetoothScannerReceiver { device ->
        if (device.bondState != BOND_BONDED) {
            _bluetoothScannedDevices.update {
                val newDevice = device.mapToFoundDevice()
                val scanningDevices = _bluetoothScannedDevices.value.scanningDevices.run {
                    if (!containsByAddress(newDevice)) {
                        plus(newDevice)
                    } else {
                        this
                    }
                }
                _bluetoothScannedDevices.value.copy(
                    scanningDevices = scanningDevices,
                    pairedDevices = _bluetoothScannedDevices.value.pairedDevices.removeDevice(newDevice)
                )
            }
        }
    }

    private val updateBondedReceiver = bluetoothBondedReceiver { device ->
        CoroutineScope(Dispatchers.Default).launch {
            withConnectPermission(nonPermission = { }) {
                bluetoothAdapter?.let { adapter ->
                    updatePairedDevices(adapter.bondedDevices, device)
                }
            }
        }
    }

    val serviceState = callbackFlow {

        val adapter = bluetoothAdapter

        if (adapter == null || !context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            trySend(BluetoothServiceState.NotSupported)
            awaitClose { }
        } else {
            val receiver = bluetoothStateReceiver { isConnected ->
                if (isConnected) {
                    trySend(BluetoothServiceState.BluetoothOn)
                } else {
                    trySend(BluetoothServiceState.BluetoothOff)
                }
            }

            val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            val initialState = when (adapter.state) {
                BluetoothAdapter.STATE_ON -> BluetoothServiceState.BluetoothOn
                else -> BluetoothServiceState.BluetoothOff
            }

            trySend(initialState)

            awaitClose {
                context.unregisterReceiver(receiver)
            }
        }
    }.flowOn(Dispatchers.Default)

    init {
        context.registerReceiver(updateBondedReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
        context.registerReceiver(scannerReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    suspend fun startScanning(): Unit = withContext(Dispatchers.IO) {
        withScanPermission(nonPermission = { _scanningState.emit(BluetoothScanningState.NonPermission) }) {
            withConnectPermission(nonPermission = { _scanningState.emit(BluetoothScanningState.NonPermission) }) {
                val isSuccess = bluetoothAdapter?.let { adapter ->
                    updatePairedDevices(adapter.bondedDevices)
                    if (adapter.isDiscovering) {
                        adapter.cancelDiscovery()
                    }
                    adapter.startDiscovery()
                } ?: false
                if (isSuccess) {
                    _bluetoothScannedDevices.emit(_bluetoothScannedDevices.value.copy(scanningDevices = emptyList()))
                    _scanningState.emit(BluetoothScanningState.InProgress)
                } else {
                    _scanningState.emit(BluetoothScanningState.Error)
                }
            }
        }
    }

    suspend fun stopScanning(): Unit = withContext(Dispatchers.IO) {
        withScanPermission(nonPermission = { _scanningState.emit(BluetoothScanningState.NonPermission) }) {
            bluetoothAdapter?.cancelDiscovery()
            _scanningState.emit(BluetoothScanningState.None)
        }
    }

    private fun updatePairedDevices(devices: Set<BluetoothDevice>, scanningDeviceUpdate: BluetoothDevice? = null) {
        _bluetoothScannedDevices.update {

            val pairedDevices = devices.map { device -> device.mapToFoundDevice() }

            val scanningDevices = bluetoothScannedDevices.value.scanningDevices.filter { scanningDevice ->
                pairedDevices.find { it.address == scanningDevice.address } == null
            }.toMutableList()

            if (scanningDeviceUpdate != null) {
                val device = scanningDeviceUpdate.mapToFoundDevice()
                scanningDevices.replaceAll { if (it.address == device.address) device else it }
            }

            _bluetoothScannedDevices.value.copy(
                pairedDevices = pairedDevices,
                scanningDevices = scanningDevices
            )
        }
    }

    suspend fun requestPairDevice(deviceAddress: DeviceAddress): BluetoothPairingEvent = withContext(Dispatchers.IO) {
        withConnectPermission(nonPermission = { return@withContext BluetoothPairingEvent.NonPermission }) {
            val adapter = bluetoothManager.adapter ?: return@withContext BluetoothPairingEvent.Error
            try {
                val remoteDevice = adapter.getRemoteDevice(deviceAddress.value)
                if (remoteDevice.bondState != BOND_BONDED) {
                    if (remoteDevice.createBond()) {
                        return@withContext BluetoothPairingEvent.PairingRequested
                    } else {
                        return@withContext BluetoothPairingEvent.Error
                    }
                } else {
                    return@withContext BluetoothPairingEvent.DeviceAlreadyPaired
                }
            } catch (e: IOException) {
                return@withContext BluetoothPairingEvent.Error
            }
        }
        return@withContext BluetoothPairingEvent.Error
    }

    fun release() {
        context.unregisterReceiver(updateBondedReceiver)
        context.unregisterReceiver(scannerReceiver)
    }

    private suspend fun requestPermissionIfNeeded(permission: String): Boolean {
        return if (!permissionService.isPermissionGranted(permission)) {
            permissionService.requestPermission(permission) is SinglePermissionResult.Granted
        } else {
            true
        }
    }

    private fun List<BluetoothFoundDevice>.containsByAddress(device: BluetoothFoundDevice): Boolean {
        return find { it.address == device.address } != null
    }

    private fun List<BluetoothFoundDevice>.removeDevice(device: BluetoothFoundDevice): List<BluetoothFoundDevice> {
        return filter { it.address != device.address }
    }

    private suspend inline fun withScanPermission(
        nonPermission: () -> Unit,
        block: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !requestPermissionIfNeeded(Manifest.permission.BLUETOOTH_SCAN)) {
            nonPermission()
        } else {
            block()
        }
    }

    private suspend inline fun withConnectPermission(
        nonPermission: () -> Unit,
        block: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !requestPermissionIfNeeded(Manifest.permission.BLUETOOTH_CONNECT)) {
            nonPermission()
        } else {
            block()
        }
    }
}
