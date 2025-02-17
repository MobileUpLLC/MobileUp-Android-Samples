package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.fakeSimpleDialogControl

class FakeCollapsingToolbarMenuComponent : CollapsingToolbarMenuComponent {
    override val infoDialog: SimpleDialogControl<Unit> = fakeSimpleDialogControl(Unit)

    override fun onInfoClick(): Unit = Unit
    override fun onCommonClick() = Unit
    override fun onCustomClick() = Unit
}
