package ru.mobileup.samples.features.qr_code.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.features.qr_code.presentation.generator.QrCodeGeneratorUi
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainUi
import ru.mobileup.samples.features.qr_code.presentation.scanner.QrCodeScannerUi

@Composable
fun QrCodeUi(
    component: QrCodeComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.stack.collectAsState()

    Children(stack, modifier) {
        when (val instance = it.instance) {
            is QrCodeComponent.Child.Generator -> QrCodeGeneratorUi(instance.component)
            is QrCodeComponent.Child.Main -> QrCodeMainUi(instance.component)
            is QrCodeComponent.Child.Scanner -> QrCodeScannerUi(instance.component)
        }
    }
}

@Preview
@Composable
private fun QrCodeUiPreview() {
    AppTheme {
        QrCodeUi(FakeQrCodeComponent())
    }
}
