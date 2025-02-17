package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

interface CollapsingToolbarExampleComponent {
    val type: CollapsingToolbarLayoutType
    val isLazyLayout: Boolean
}
