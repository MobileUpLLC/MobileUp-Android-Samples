package ru.mobileup.samples.features.navigation.alert_dialogs

import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.dialog.dialogControl
import ru.mobileup.samples.core.dialog.simple.simpleDialogControl
import ru.mobileup.samples.core.dialog.standard.DialogButton
import ru.mobileup.samples.core.dialog.standard.StandardDialogData
import ru.mobileup.samples.core.dialog.standard.standardDialogControl
import ru.mobileup.samples.core.utils.InputControl
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.navigation.createNavigationCustomDialogComponent
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent
import ru.mobileup.samples.core.R as CoreR

class RealNavigationAlertDialogsComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, NavigationAlertDialogsComponent {

    override val nameInputControl = InputControl(
        initialText = "Example name"
    )

    // Simple dialog control logic

    override val simpleDialogControl = simpleDialogControl<String>(
        key = "simpleDialogControl"
    )

    override fun onShowSimpleDialogControlClick() {
        simpleDialogControl.show(nameInputControl.text.value)
    }

    override fun onClearTextClick() = nameInputControl.setText("")

    // StandardDialogControl logic

    override val standardDialogControl = standardDialogControl(
        key = "standardDialogControl"
    )

    override fun onShowStandardDialogControlClick() {
        standardDialogControl.show(
            StandardDialogData(
                title = R.string.navigation_dialogs_standard_dialog_title.strResDesc(),
                message = R.string.navigation_dialogs_standard_dialog_text.strResDesc(),
                confirmButton = DialogButton(
                    text = R.string.navigation_clear.strResDesc(),
                    action = {
                        nameInputControl.setText("")
                        standardDialogControl.dismiss()
                    }
                ),
                dismissButton = DialogButton(
                    text = CoreR.string.common_close.strResDesc(),
                    action = standardDialogControl::dismiss
                )
            )
        )
    }

    // Custom dialog control logic

    override val customDialogControl = dialogControl(
        key = "customDialogControl",
        dialogComponentFactory = { config, componentContext, _ ->
            componentFactory.createNavigationCustomDialogComponent(
                componentContext,
                config,
                ::onCustomDialogOutput
            )
        }
    )

    private fun onCustomDialogOutput(output: NavigationCustomDialogComponent.Output) {
        when (output) {
            NavigationCustomDialogComponent.Output.CloseRequest -> customDialogControl.dismiss()
            NavigationCustomDialogComponent.Output.SubmitRequest -> {
                nameInputControl.setText("")
                customDialogControl.dismiss()
            }
        }
    }

    override fun onShowCustomDialogControlClick() {
        customDialogControl.show(
            NavigationCustomDialogComponent.Config(nameInputControl.text.value)
        )
    }
}