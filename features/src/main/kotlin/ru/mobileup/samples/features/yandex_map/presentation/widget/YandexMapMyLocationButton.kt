package ru.mobileup.samples.features.yandex_map.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.features.R

@Composable
fun YandexMapMyLocationButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isEnabled) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
                    .border(1.dp, Color.White, CircleShape)
                    .padding(8.dp),
                painter = painterResource(R.drawable.ic_location_filled_24),
                contentDescription = null
            )
        } else {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
    }
}
