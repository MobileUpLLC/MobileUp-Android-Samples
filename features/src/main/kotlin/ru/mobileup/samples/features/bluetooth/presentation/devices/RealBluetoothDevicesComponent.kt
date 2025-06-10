package ru.mobileup.samples.features.bluetooth.presentation.devices

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.bluetooth.domain.BluetoothScannedDevices
import ru.mobileup.samples.features.bluetooth.domain.BluetoothService
import ru.mobileup.samples.features.bluetooth.domain.BluetoothServiceState
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress
import ru.mobileup.samples.features.bluetooth.domain.events.BluetoothPairingEvent

class RealBluetoothDevicesComponent(
    componentContext: ComponentContext,
    private val bluetoothService: BluetoothService,
    private val activityProvider: ActivityProvider,
    private val messageService: MessageService
) : ComponentContext by componentContext, BluetoothDevicesComponent {

    override val bluetoothState = bluetoothService
        .serviceState
        .stateIn(
            scope = componentScope,
            initialValue = BluetoothServiceState.BluetoothOff,
            started = SharingStarted.Eagerly
        )

    override val bluetoothScannedDevices = bluetoothService
        .bluetoothScannedDevices
        .stateIn(
            scope = componentScope,
            initialValue = BluetoothScannedDevices.empty,
            started = SharingStarted.Eagerly
        )

    override val scanningState = bluetoothService.scanningState

    init {
        bluetoothState
            .filterIsInstance<BluetoothServiceState.BluetoothOn>()
            .onEach { startScanning() }
            .launchIn(componentScope)

        doOnDestroy { bluetoothService.release() }
    }

    override fun openBluetoothPermission() {
        activityProvider.activity?.let { activity ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }
            activity.startActivity(intent)
        }
    }

    override fun startScanning() {
        componentScope.launch {
            bluetoothService.startScanning()
            delay(SCANNING_DELAY)
            bluetoothService.stopScanning()
        }
    }

    override fun onClickDevice(address: DeviceAddress) {
        componentScope.launch {
            when (bluetoothService.requestPairDevice(address)) {
                BluetoothPairingEvent.DeviceAlreadyPaired -> {
                    messageService.showMessage(
                        Message(text = R.string.bluetooth_device_already_paired.strResDesc())
                    )
                }
                BluetoothPairingEvent.Error -> {
                    messageService.showMessage(
                        Message(text = R.string.bluetooth_pairing_error.strResDesc())
                    )
                }
                BluetoothPairingEvent.NonPermission -> {
                    messageService.showMessage(
                        Message(
                            text = R.string.bluetooth_pairing_non_permissions.strResDesc(),
                            actionTitle = R.string.bluetooth_open_settings.strResDesc(),
                            action = { openBluetoothPermission() }
                        )
                    )
                }
                BluetoothPairingEvent.PairingRequested -> Unit
            }
        }
    }

    companion object {
        private const val SCANNING_DELAY = 10_000L
    }
}