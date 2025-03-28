package ru.mobileup.samples.features.pin_code.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.features.pin_code.presentation.create.CreatePinCodeUi
import ru.mobileup.samples.features.pin_code.presentation.settings_main.PinCodeSettingsMainUi

@Composable
fun PinCodeSettingsUi(
    component: PinCodeSettingsComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is PinCodeSettingsComponent.Child.Main -> PinCodeSettingsMainUi(instance.component)
            is PinCodeSettingsComponent.Child.Create -> CreatePinCodeUi(instance.component)
        }
    }
}