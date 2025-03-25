package ru.mobileup.samples.features.yandex_map.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.features.R

@Composable
fun YandexMapZoomButtons(
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgModifier = Modifier
        .size(48.dp)
        .background(Color.White.copy(alpha = 0.7f), CircleShape)
        .border(1.dp, Color.White, CircleShape)
        .padding(8.dp)
    Column(
        modifier = modifier
    ) {
        IconButton(
            onClick = onZoomInClick,
            modifier = Modifier
        ) {
            Icon(
                modifier = bgModifier,
                painter = painterResource(R.drawable.ic_plus_16),
                contentDescription = null
            )
        }
        IconButton(
            onClick = onZoomOutClick
        ) {
            Icon(
                modifier = bgModifier,
                painter = painterResource(R.drawable.ic_minus_16),
                contentDescription = null
            )
        }
    }
}