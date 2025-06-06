package ru.mobileup.samples.features.bluetooth.presentation.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.bluetooth.domain.BluetoothServiceState

@Composable
fun BluetoothStateUi(
    state: BluetoothServiceState,
    modifier: Modifier = Modifier
) {

    val backgroundColor = animateColorAsState(
        targetValue = when (state) {
            BluetoothServiceState.BluetoothOff -> CustomTheme.colors.button.primaryDisabled
            BluetoothServiceState.BluetoothOn -> CustomTheme.colors.text.positive.copy(alpha = 0.5f)
            BluetoothServiceState.NotSupported -> CustomTheme.colors.icon.secondary.copy(alpha = 0.5f)
        }
    )

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            )
            .drawBehind { drawRect(backgroundColor.value) }
            .then(modifier)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(
                when (state) {
                    BluetoothServiceState.BluetoothOff -> R.string.bluetooth_of
                    BluetoothServiceState.BluetoothOn -> R.string.bluetooth_on
                    BluetoothServiceState.NotSupported -> R.string.bluetooth_not_supported
                }
            ),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBluetoothStateUiBluetoothOn() {
    AppTheme {
        BluetoothStateUi(
            state = BluetoothServiceState.BluetoothOn,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBluetoothStateUiBluetoothOff() {
    AppTheme {
        BluetoothStateUi(
            state = BluetoothServiceState.BluetoothOff,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBluetoothStateUiNotSupported() {
    AppTheme {
        BluetoothStateUi(
            state = BluetoothServiceState.NotSupported,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}