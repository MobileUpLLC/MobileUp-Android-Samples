package ru.mobileup.samples.features.pin_code.presentation.settings

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.features.pin_code.presentation.settings_main.FakePinCodeSettingsMainComponent

class FakePinCodeSettingsComponent : PinCodeSettingsComponent {
    override val childStack: StateFlow<ChildStack<*, PinCodeSettingsComponent.Child>> =
        createFakeChildStackStateFlow(
            PinCodeSettingsComponent.Child.Main(
                FakePinCodeSettingsMainComponent()
            )
        )
}