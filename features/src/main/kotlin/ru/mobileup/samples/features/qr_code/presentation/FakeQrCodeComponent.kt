package ru.mobileup.samples.features.qr_code.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStack
import ru.mobileup.samples.features.qr_code.presentation.QrCodeComponent.Child
import ru.mobileup.samples.features.qr_code.presentation.generator.FakeQrCodeGeneratorComponent

class FakeQrCodeComponent : QrCodeComponent {
    override val stack: StateFlow<ChildStack<*, Child>> =
        MutableStateFlow(
            createFakeChildStack(
                Child.Generator(
                    FakeQrCodeGeneratorComponent()
                )
            )
        )
}
