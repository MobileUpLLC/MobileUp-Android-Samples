package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import com.arkivanov.decompose.ComponentContext
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.simpleDialogControl

class RealCollapsingToolbarMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (CollapsingToolbarMenuComponent.Output) -> Unit
) : CollapsingToolbarMenuComponent, ComponentContext by componentContext {

    override val infoDialog: SimpleDialogControl<Unit> = simpleDialogControl(
        key = "collapsing_toolbar_menu_info_dialog",
    )

    override fun onCommonClick() {
        onOutput(CollapsingToolbarMenuComponent.Output.ExampleRequested(true))
    }

    override fun onCustomClick() {
        onOutput(CollapsingToolbarMenuComponent.Output.ExampleRequested(false))
    }

    override fun onInfoClick() {
        infoDialog.show(Unit)
    }
}
