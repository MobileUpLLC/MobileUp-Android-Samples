package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

class FakeCollapsingToolbarExampleComponent : CollapsingToolbarExampleComponent {
    override val type: CollapsingToolbarLayoutType = CollapsingToolbarLayoutType.SimpleBoth
    override val isLazyLayout: Boolean = true
}