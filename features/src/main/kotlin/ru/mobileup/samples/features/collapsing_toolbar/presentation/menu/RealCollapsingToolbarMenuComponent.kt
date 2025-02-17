package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.simpleDialogControl
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

class RealCollapsingToolbarMenuComponent(
    componentContext: ComponentContext,
    private val onOutput: (CollapsingToolbarMenuComponent.Output) -> Unit
) : CollapsingToolbarMenuComponent, ComponentContext by componentContext {

    override val infoDialog: SimpleDialogControl<Unit> = simpleDialogControl(
        key = "collapsing_toolbar_menu_info_dialog",
    )

    override val layoutType = MutableStateFlow(CollapsingToolbarLayoutType.SimpleNormal)

    override val isLazyLayout = MutableStateFlow(false)

    override fun onExampleClick() {
        openExample(isAdvanced = false)
    }

    override fun onInfoClick() {
        infoDialog.show(Unit)
    }

    override fun onLazyLayoutCheckedChange(value: Boolean) {
        isLazyLayout.value = value
    }

    override fun onLayoutTypeClicked(type: CollapsingToolbarLayoutType) {
        layoutType.value = type
    }

    private fun openExample(isAdvanced: Boolean) = onOutput(
        CollapsingToolbarMenuComponent.Output.ExampleRequested(
            isLazyLayout = isLazyLayout.value,
            type = layoutType.value
        )
    )
}
