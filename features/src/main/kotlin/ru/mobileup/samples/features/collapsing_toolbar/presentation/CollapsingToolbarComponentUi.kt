package ru.mobileup.samples.features.collapsing_toolbar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.CollapsingToolbarMenuComponentUi
import ru.mobileup.samples.features.collapsing_toolbar.presentation.example.CollapsingToolbarExampleComponentUi

@Composable
fun CollapsingToolbarComponentUi(
    component: CollapsingToolbarComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is CollapsingToolbarComponent.Child.Menu -> CollapsingToolbarMenuComponentUi(instance.component)
            is CollapsingToolbarComponent.Child.Example -> CollapsingToolbarExampleComponentUi(instance.component)
        }
    }
}