package ru.mobileup.samples.features.pin_code

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.pin_code.data.PinCodeStorage
import ru.mobileup.samples.features.pin_code.data.PinCodeStorageImpl
import ru.mobileup.samples.features.pin_code.presentation.check.CheckPinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.check.RealCheckPinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.CheckPinCodeManagementComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.RealCheckPinCodeManagementComponent
import ru.mobileup.samples.features.pin_code.presentation.create.CreatePinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.create.RealCreatePinCodeComponent
import ru.mobileup.samples.features.pin_code.presentation.settings.PinCodeSettingsComponent
import ru.mobileup.samples.features.pin_code.presentation.settings.RealPinCodeSettingsComponent
import ru.mobileup.samples.features.pin_code.presentation.settings_main.PinCodeSettingsMainComponent
import ru.mobileup.samples.features.pin_code.presentation.settings_main.RealPinCodeSettingsMainComponent

val pinCodeModule = module {
    single<PinCodeStorage> { PinCodeStorageImpl(get()) }
}

fun ComponentFactory.createCheckPinCodeComponent(
    componentContext: ComponentContext,
    onOutput: (CheckPinCodeComponent.Output) -> Unit
): CheckPinCodeComponent {
    return RealCheckPinCodeComponent(
        componentContext,
        get(),
        get(),
        get(),
        get(),
        get(),
        onOutput
    )
}

fun ComponentFactory.createCreatePinCodeComponent(
    componentContext: ComponentContext,
    onOutput: (CreatePinCodeComponent.Output) -> Unit,
): CreatePinCodeComponent {
    return RealCreatePinCodeComponent(
        componentContext,
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        onOutput
    )
}

fun ComponentFactory.createCheckPinCodeManagementComponent(
    componentContext: ComponentContext
): CheckPinCodeManagementComponent {
    return RealCheckPinCodeManagementComponent(
        componentContext,
        get(),
        get()
    )
}

fun ComponentFactory.createPinCodeSettingsComponent(
    componentContext: ComponentContext
): PinCodeSettingsComponent {
    return RealPinCodeSettingsComponent(componentContext, get())
}

fun ComponentFactory.createPinCodeSettingsMainComponent(
    componentContext: ComponentContext,
    onOutput: (PinCodeSettingsMainComponent.Output) -> Unit
): PinCodeSettingsMainComponent {
    return RealPinCodeSettingsMainComponent(
        componentContext,
        onOutput,
        get(),
        get(),
        get()
    )
}
