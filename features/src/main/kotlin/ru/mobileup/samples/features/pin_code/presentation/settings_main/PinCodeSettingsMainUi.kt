package ru.mobileup.samples.features.pin_code.presentation.settings_main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.utils.dispatchOnBackPressed

@Composable
fun PinCodeSettingsMainUi(
    component: PinCodeSettingsMainComponent,
    modifier: Modifier = Modifier
) {

    val isPinCodeEnabled by component.isPinCodeEnabled.collectAsState()
    val isBiometricEnabled by component.isBiometricEnabled.collectAsState()
    Scaffold(
        modifier = modifier
            .systemBarsPadding(),
        topBar = { Toolbar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Row {
                Text(
                    text = "Pin code",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isPinCodeEnabled,
                    onCheckedChange = component::onPinCodeEnabledChanged
                )
            }

            Row {
                Text(
                    text = "Biometric",
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isBiometricEnabled,
                    onCheckedChange = component::onBiometricEnabledChanged
                )
            }
        }
    }
}

@Composable
private fun Toolbar(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(modifier = modifier) {
        IconButton(onClick = { dispatchOnBackPressed(context) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null
            )
        }
    }
}