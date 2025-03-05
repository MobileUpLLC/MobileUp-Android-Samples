package ru.mobileup.samples.features.navigation.bottom_sheets

import kotlinx.coroutines.GlobalScope
import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.samples.core.dialog.DialogControl
import ru.mobileup.samples.core.dialog.fakeDialogControl
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.fakeSimpleDialogControl
import ru.mobileup.samples.features.navigation.custom_dialog.FakeNavigationCustomDialogComponent
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent

class FakeNavigationBottomSheetsComponent : NavigationBottomSheetsComponent {
    override val simpleDialogControl: SimpleDialogControl<String> = fakeSimpleDialogControl("")

    override val customDialogControl: DialogControl<NavigationCustomDialogComponent.Config, NavigationCustomDialogComponent> =
        fakeDialogControl(
            NavigationCustomDialogComponent.Config("John Snow"),
            FakeNavigationCustomDialogComponent()
        )

    override val nameInputControl: InputControl = InputControl(GlobalScope)

    override fun onShowSimpleDialogControlClick(): Unit = Unit

    override fun onShowCustomDialogControlClick(): Unit = Unit

    override fun onClearTextClick(): Unit = Unit
}
