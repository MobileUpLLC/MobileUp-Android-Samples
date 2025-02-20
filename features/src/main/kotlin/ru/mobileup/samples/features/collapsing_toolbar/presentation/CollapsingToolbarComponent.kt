package ru.mobileup.samples.features.collapsing_toolbar.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.CollapsingToolbarMenuComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.example.CollapsingToolbarExampleComponent

interface CollapsingToolbarComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Menu(val component: CollapsingToolbarMenuComponent) : Child
        data class Example(val component: CollapsingToolbarExampleComponent) : Child
    }
}
