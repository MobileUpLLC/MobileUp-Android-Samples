package ru.mobileup.samples.features.bluetooth.presentation.widget

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.bluetooth.domain.BluetoothFoundDevice
import ru.mobileup.samples.features.bluetooth.domain.BluetoothFoundDeviceType
import ru.mobileup.samples.features.bluetooth.domain.DeviceAddress

@Composable
fun BluetoothDeviceUi(
    bluetoothFoundDevice: BluetoothFoundDevice,
    modifier: Modifier = Modifier,
    onClick: ((DeviceAddress) -> Unit)? = null,
) {

    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick(bluetoothFoundDevice.address) }
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CustomTheme.colors.chat.secondary)
            .then(clickableModifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = bluetoothDeviceIcon(type = bluetoothFoundDevice.type),
            contentDescription = "bluetooth_device_type_icon"
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = bluetoothFoundDevice.name ?: stringResource(R.string.bluetooth_device_unknown_name),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = CustomTheme.typography.caption.regular
            )
            Text(
                text = bluetoothFoundDevice.address.value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = CustomTheme.typography.caption.small,
                color = CustomTheme.colors.text.secondary
            )
        }

        if (bluetoothFoundDevice.isPairingProcess) {
            val infiniteTransition = rememberInfiniteTransition()
            val animatedRotate = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = InfiniteRepeatableSpec(
                    animation = tween(durationMillis = 1500),
                    repeatMode = RepeatMode.Restart
                )
            )

            Icon(
                painter = painterResource(R.drawable.ic_rotate),
                contentDescription = "refresh_scanning_devices",
                tint = CustomTheme.colors.text.positive,
                modifier = Modifier
                    .graphicsLayer { rotationZ = animatedRotate.value }
            )
        }
    }
}

@Composable
private fun bluetoothDeviceIcon(type: BluetoothFoundDeviceType): Painter {
    return painterResource(
        when (type) {
            BluetoothFoundDeviceType.AudioVideoOutput -> R.drawable.ic_media_output
            BluetoothFoundDeviceType.Computer -> R.drawable.ic_computer
            BluetoothFoundDeviceType.Phone -> R.drawable.ic_smart_phone
            BluetoothFoundDeviceType.Unknown -> R.drawable.ic_bluetooth
        }
    )
}

@Preview
@Composable
private fun PreviewBluetoothDeviceUi() {
    AppTheme {
        BluetoothDeviceUi(
            BluetoothFoundDevice(
                address = DeviceAddress("6E:F5:35:B2:17:A4"),
                name = "Samsung (Name)",
                type = BluetoothFoundDeviceType.Phone,
                isPairingProcess = true
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}