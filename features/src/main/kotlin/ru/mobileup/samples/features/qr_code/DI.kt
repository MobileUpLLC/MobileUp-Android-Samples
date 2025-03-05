package ru.mobileup.samples.features.qr_code

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.qr_code.presentation.QrCodeComponent
import ru.mobileup.samples.features.qr_code.presentation.RealQrCodeComponent
import ru.mobileup.samples.features.qr_code.presentation.generator.QrCodeGeneratorComponent
import ru.mobileup.samples.features.qr_code.presentation.generator.RealQrCodeGeneratorComponent
import ru.mobileup.samples.features.qr_code.presentation.main.QrCodeMainComponent
import ru.mobileup.samples.features.qr_code.presentation.main.RealQrCodeMainComponent
import ru.mobileup.samples.features.qr_code.presentation.scanner.QrCodeScannerComponent
import ru.mobileup.samples.features.qr_code.presentation.scanner.RealQrCodeScannerComponent

fun ComponentFactory.createQrCodeGeneratorComponent(
    componentContext: ComponentContext
): QrCodeGeneratorComponent {
    return RealQrCodeGeneratorComponent(componentContext)
}

fun ComponentFactory.createQrCodeScannerComponent(
    componentContext: ComponentContext
): QrCodeScannerComponent {
    return RealQrCodeScannerComponent(componentContext, get(), get(), get())
}

fun ComponentFactory.createQrCodeMainComponent(
    componentContext: ComponentContext,
    onOutput: (QrCodeMainComponent.Output) -> Unit
): QrCodeMainComponent {
    return RealQrCodeMainComponent(componentContext, onOutput)
}

fun ComponentFactory.createQrCodeComponent(
    componentContext: ComponentContext,
): QrCodeComponent {
    return RealQrCodeComponent(componentContext, get())
}
