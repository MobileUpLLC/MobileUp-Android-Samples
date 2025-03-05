package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R

@Composable
fun RenderProgressIndicator(
    progress: Float,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomTheme.colors.palette.black50)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                progress = { progress },
                color = CustomTheme.colors.palette.white,
                trackColor = Color.White.copy(0.7f)
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "${(progress * 100).toInt()}%",
                color = CustomTheme.colors.text.invert
            )
        }

        OutlinedButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp),
            onClick = onCancel
        ) {
            Text(
                text = stringResource(R.string.dismiss_btn),
                color = CustomTheme.colors.palette.white
            )
        }
    }
}

@Preview
@Composable
private fun RenderProgressIndicatorPreview() {
    AppTheme {
        RenderProgressIndicator(
            progress = 0.5f,
            onCancel = { }
        )
    }
}