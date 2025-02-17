package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.dialog.simple.SimpleDialogControl
import ru.mobileup.samples.core.dialog.simple.fakeSimpleDialogControl
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

class FakeCollapsingToolbarMenuComponent : CollapsingToolbarMenuComponent {
    override val infoDialog: SimpleDialogControl<Unit> = fakeSimpleDialogControl(Unit)
    override val layoutType: StateFlow<CollapsingToolbarLayoutType> =
        MutableStateFlow(CollapsingToolbarLayoutType.SimpleBoth)
    override val isLazyLayout: StateFlow<Boolean> = MutableStateFlow(true)

    override fun onExampleClick(): Unit = Unit
    override fun onInfoClick(): Unit = Unit
    override fun onLazyLayoutCheckedChange(value: Boolean) = Unit
    override fun onLayoutTypeClicked(type: CollapsingToolbarLayoutType) = Unit
}
