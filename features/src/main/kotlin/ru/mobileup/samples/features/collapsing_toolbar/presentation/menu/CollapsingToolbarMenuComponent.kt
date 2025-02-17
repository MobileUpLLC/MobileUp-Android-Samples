package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

interface CollapsingToolbarMenuComponent {
    val infoDialog: SimpleDialogControl<Unit>
    val layoutType: StateFlow<CollapsingToolbarLayoutType>
    val isLazyLayout: StateFlow<Boolean>

    fun onExampleClick()
    fun onInfoClick()
    fun onLazyLayoutCheckedChange(value: Boolean)
    fun onLayoutTypeClicked(type: CollapsingToolbarLayoutType)

    sealed interface Output {
        data class ExampleRequested(
            val isLazyLayout: Boolean,
            val type: CollapsingToolbarLayoutType
        ) : Output
    }
}
