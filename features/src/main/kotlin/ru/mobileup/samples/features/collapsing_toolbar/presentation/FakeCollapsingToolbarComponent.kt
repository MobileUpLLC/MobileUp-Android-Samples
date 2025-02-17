package ru.mobileup.samples.features.collapsing_toolbar.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.features.collapsing_toolbar.presentation.CollapsingToolbarComponent.Child
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.FakeCollapsingToolbarMenuComponent

class FakeCollapsingToolbarComponent : CollapsingToolbarComponent {
    override val childStack: StateFlow<ChildStack<*, Child>> =
        createFakeChildStackStateFlow(CollapsingToolbarComponent.Child.Menu(FakeCollapsingToolbarMenuComponent()))
}
