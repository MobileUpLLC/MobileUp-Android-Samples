package ru.mobileup.samples.features.collapsing_toolbar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.features.collapsing_toolbar.presentation.menu.CollapsingToolbarMenuUi
import ru.mobileup.samples.features.collapsing_toolbar.presentation.example.CollapsingToolbarExampleUi

@Composable
fun CollapsingToolbarUi(
    component: CollapsingToolbarComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is CollapsingToolbarComponent.Child.Menu -> CollapsingToolbarMenuUi(instance.component)
            is CollapsingToolbarComponent.Child.Example -> CollapsingToolbarExampleUi(instance.component)
        }
    }
}