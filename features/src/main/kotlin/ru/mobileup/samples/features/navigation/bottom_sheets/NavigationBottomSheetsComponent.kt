package ru.mobileup.samples.features.navigation.bottom_sheets

import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.samples.core.dialog.DialogControl
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent

interface NavigationBottomSheetsComponent {
    val simpleDialogControl: SimpleDialogControl<String>
    val customDialogControl: DialogControl<NavigationCustomDialogComponent.Config, NavigationCustomDialogComponent>

    val nameInputControl: InputControl

    fun onShowSimpleDialogControlClick()
    fun onShowCustomDialogControlClick()

    fun onClearTextClick()
}