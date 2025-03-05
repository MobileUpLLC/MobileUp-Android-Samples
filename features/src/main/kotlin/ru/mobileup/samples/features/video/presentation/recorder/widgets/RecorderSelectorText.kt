package ru.mobileup.samples.features.video.presentation.recorder.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun RowScope.RecorderSelectorText(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = if (isActive) {
            CustomTheme.colors.text.warning
        } else {
            CustomTheme.colors.text.invert
        },
        textAlign = TextAlign.Center,
        modifier = modifier
            .weight(1f)
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            }
    )
}

@Preview
@Composable
private fun RecorderSelectorTextPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RecorderSelectorText(
                    text = "Selector text",
                    isActive = true,
                    onClick = { }
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                RecorderSelectorText(
                    text = "Selector text",
                    isActive = false,
                    onClick = { }
                )
            }
        }
    }
}