package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun CollapsingToolbarMockItem(
    index: Int,
    modifier: Modifier = Modifier
) {
    val bgColor = remember { Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)) }
    val textColor = remember { Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(bgColor, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mock item number $index",
            color = textColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(32.dp)
        )
    }
}