package ru.mobileup.samples.features.divkit.presentation.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.div.core.view2.Div2View

@Composable
fun DivKitView(
    content: Div2View?,
    modifier: Modifier = Modifier
) {
    val isLoading = content == null
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        content?.view?.let { divView ->
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { divView },
            )
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Companion.Center)
            )
        }
    }
}