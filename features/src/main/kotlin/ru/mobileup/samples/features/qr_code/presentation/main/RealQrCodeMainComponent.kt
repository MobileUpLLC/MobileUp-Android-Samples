package ru.mobileup.samples.features.qr_code.presentation.main

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainComponent.Output.GeneratorRequested
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainComponent.Output.ScannerRequested

class RealQrCodeMainComponent(
    componentContext: ComponentContext,
    private val onOutput: (QrCodeMainComponent.Output) -> Unit
) : ComponentContext by componentContext, QrCodeMainComponent {

    override fun onGeneratorClick() = onOutput(GeneratorRequested)

    override fun onScannerClick() = onOutput(ScannerRequested)
}