package ru.mobileup.samples.features.navigation.bottom_sheets

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.dialog.dialogControl
import ru.mobileup.samples.core.dialog.simple.simpleDialogControl
import ru.mobileup.samples.core.utils.InputControl
import ru.mobileup.samples.features.navigation.createNavigationCustomDialogComponent
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogComponent

class RealNavigationBottomSheetsComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, NavigationBottomSheetsComponent {

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