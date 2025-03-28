package ru.mobileup.samples.features.pin_code.presentation.settings

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.pin_code.presentation.create.CreatePinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.settings_main.PinCodeSettingsMainComponent

interface PinCodeSettingsComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Main(val component: PinCodeSettingsMainComponent) : Child
        data class Create(val component: CreatePinCodeComponent) : Child
    }
}