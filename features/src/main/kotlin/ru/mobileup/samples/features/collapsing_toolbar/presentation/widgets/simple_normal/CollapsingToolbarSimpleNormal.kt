package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.simple_normal

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.utils.dispatchOnBackPressed

@Composable
fun CollapsingToolbarSimpleNormal(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    title: String = "SimpleNormal",
    leadingIconImageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
) {
    val density = LocalDensity.current
    val ctx = LocalContext.current

    var leadingIconWidth by remember { mutableIntStateOf(0) }
    val params = remember(scrollValue) {
        SimpleNormalToolbarParams(
            scrollValue = scrollValue,
            leadingIconWidthPx = leadingIconWidth,
            density = density.density
        )
    }
    val transitionParams = updateTransition(params)
    val containerHeight by transitionParams.animateDp { it.container.height.dp }
    val leadingIconVerticalBias by transitionParams.animateFloat { it.leadingIcon.verticalBias }
    val titleVerticalBias by transitionParams.animateFloat { it.title.verticalBias }
    val titleHorizontalPadding by transitionParams.animateDp { it.title.horizontalPadding.dp }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = containerHeight)
            .padding(horizontal = 16.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height - 4 * density.density),
                    end = Offset(size.width, size.height - 4 * density.density)
                )
            },
    ) {

        Icon(
            imageVector = leadingIconImageVector,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { dispatchOnBackPressed(ctx) })
                .size(24.dp)
                .align(BiasAlignment(-1f, leadingIconVerticalBias))
                .onSizeChanged { leadingIconWidth = it.width }
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = titleHorizontalPadding, vertical = 8.dp)
                .align(BiasAlignment(-1f, titleVerticalBias))
        )
    }
}
