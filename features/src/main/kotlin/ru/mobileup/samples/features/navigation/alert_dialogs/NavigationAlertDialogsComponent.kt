package ru.mobileup.samples.features.navigation.alert_dialogs

import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.samples.core.dialog.DialogControl
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.standard.StandardDialogControl
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent

interface NavigationAlertDialogsComponent {
    val simpleDialogControl: SimpleDialogControl<String>
    val standardDialogControl: StandardDialogControl
    val customDialogControl: DialogControl<NavigationCustomDialogComponent.Config, NavigationCustomDialogComponent>

    val nameInputControl: InputControl

    fun onShowSimpleDialogControlClick()
    fun onShowStandardDialogControlClick()
    fun onShowCustomDialogControlClick()

    fun onClearTextClick()
}