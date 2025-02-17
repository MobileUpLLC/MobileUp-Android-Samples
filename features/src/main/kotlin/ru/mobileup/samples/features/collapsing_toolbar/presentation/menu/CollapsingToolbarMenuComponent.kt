package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl

interface CollapsingToolbarMenuComponent {
    val infoDialog: SimpleDialogControl<Unit>

    fun onInfoClick()
    fun onCommonClick()
    fun onCustomClick()

    sealed interface Output {
        data class ExampleRequested(val isCommon: Boolean) : Output
    }
}
