package ru.mobileup.samples.features.document.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.features.document.presentation.menu.DocumentMenuUi
import ru.mobileup.samples.features.document.presentation.preview.DocumentPreviewUi

@Composable
fun DocumentUi(
    component: DocumentComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(slide()),
        modifier = modifier
    ) { child ->
        when (val instance = child.instance) {
            is DocumentComponent.Child.Menu -> DocumentMenuUi(instance.component)
            is DocumentComponent.Child.Preview -> DocumentPreviewUi(instance.component)
        }
    }
}

@Preview
@Composable
private fun DocumentUiPreview() {
    AppTheme {
        DocumentUi(FakeDocumentComponent())
    }
}