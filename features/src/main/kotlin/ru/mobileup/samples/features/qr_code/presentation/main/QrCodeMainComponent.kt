package ru.mobileup.samples.features.qr_code.presentation.main

interface QrCodeMainComponent {

    fun onGeneratorClick()
    fun onScannerClick()

    sealed interface Output {
        data object GeneratorRequested : Output
        data object ScannerRequested : Output
    }
}