package ru.mobileup.samples.features.remote_transfer.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun RemoteProcessingIndicator(
    bytesProcessed: Long,
    bytesTotal: Long,
    modifier: Modifier = Modifier
) {
    val progressString by remember(bytesProcessed, bytesTotal) {
        derivedStateOf {
            formatFileSize(bytesProcessed) + " / " + formatFileSize(bytesTotal)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = { bytesProcessed.toFloat() / bytesTotal },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = progressString,
            color = CustomTheme.colors.text.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

private fun formatFileSize(sizeBytes: Long): String {
    return when {
        sizeBytes >= 1024 * 1024 -> "%.1fmB".format(sizeBytes / (1024.0 * 1024.0))
        sizeBytes >= 1024 -> "${(sizeBytes / 1024).toInt()}kB"
        else -> "${sizeBytes}B"
    }
}