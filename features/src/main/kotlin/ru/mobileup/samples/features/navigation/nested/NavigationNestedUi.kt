package ru.mobileup.samples.features.navigation.nested

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.features.navigation.nested.leaf.NavigationNestedLeafUi
import ru.mobileup.samples.features.navigation.nested.main.NavigationNestedMainUi

@Composable
fun NavigationNestedUi(
    component: NavigationNestedComponent,
    onBottomBarVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    val stack by component.stack.collectAsState()

    Children(stack, modifier.padding(paddingValues)) {

        SideEffect {
            onBottomBarVisibilityChange(it.instance !is NavigationNestedComponent.Child.LeafWithoutBottomBar)
        }

        when (val instance = it.instance) {
            is NavigationNestedComponent.Child.LeafWithBottomBar -> NavigationNestedLeafUi(instance.component)
            is NavigationNestedComponent.Child.LeafWithoutBottomBar -> NavigationNestedLeafUi(
                instance.component
            )

            is NavigationNestedComponent.Child.Main -> NavigationNestedMainUi(instance.component)
        }
    }
}

@Preview
@Composable
private fun NavigationNestedUiPreview() {
    AppTheme {
        NavigationNestedUi(FakeNavigationNestedComponent(), {})
    }
}