package ru.mobileup.samples.features.qr_code.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.qr_code.presentation.generator.QrCodeGeneratorComponent
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainComponent
import ru.mobileup.samples.features.qr_code.presentation.scanner.QrCodeScannerComponent

interface QrCodeComponent : PredictiveBackComponent {

    val stack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Generator(val component: QrCodeGeneratorComponent) : Child
        class Scanner(val component: QrCodeScannerComponent) : Child
        class Main(val component: QrCodeMainComponent) : Child
    }
}