package ru.mobileup.samples.features.bluetooth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.core.utils.predictiveBackAnimation
import ru.mobileup.samples.features.bluetooth.presentation.devices.BluetoothDevicesUi
import ru.mobileup.samples.features.bluetooth.presentation.menu.BluetoothMenuUi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun BluetoothUi(
    component: BluetoothComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(
        modifier = modifier,
        stack = childStack,
        animation = component.predictiveBackAnimation(),
    ) { child ->
        when (val instance = child.instance) {
            is BluetoothComponent.Child.Menu -> BluetoothMenuUi(instance.component)
            is BluetoothComponent.Child.Devices -> BluetoothDevicesUi(instance.component)
        }
    }
}