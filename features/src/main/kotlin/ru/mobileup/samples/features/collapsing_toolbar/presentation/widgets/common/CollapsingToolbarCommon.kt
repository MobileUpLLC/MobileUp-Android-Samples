package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun CollapsingToolbarCommon(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    title: String = "Раз два три четыре",
    leadingIcon: @Composable (BoxScope.() -> Unit)? = { LeadingIconExample() },
    trailingIcons: @Composable (RowScope.() -> Unit)? = { TrailingIconsExample() }
) {
    val density = LocalDensity.current

    var leadingIconWidth by remember { mutableIntStateOf(0) }
    var trailingIconsWidth by remember { mutableIntStateOf(0) }
    val params = remember(scrollValue) {
        CollapsingToolbarCommonParams(
            scrollValue = scrollValue,
            leadingIconWidthPx = leadingIconWidth,
            trailingIconsWidthPx = trailingIconsWidth,
            density = density.density
        )
    }
    val containerHeight by animateFloatAsState(params.container.height)
    val iconsVerticalBias by animateFloatAsState(params.icons.verticalBias)
    val titleVerticalBias by animateFloatAsState(params.title.verticalBias)
    val titleStartPadding by animateFloatAsState(params.title.startPadding)
    val titleEndPadding by animateFloatAsState(params.title.endPadding)
    val titleSize by animateFloatAsState(params.title.size)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = containerHeight.dp)
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

        leadingIcon?.let {
            Box(
                modifier = Modifier
                    .sizeIn(maxHeight = params.icons.maxSize.dp, maxWidth = params.icons.maxSize.dp)
                    .align(BiasAlignment(-1f, iconsVerticalBias))
                    .onSizeChanged { leadingIconWidth = it.width },
                contentAlignment = Alignment.Center
            ) {
                it()
            }
        }

        Text(
            text = title,
            style = CustomTheme.typography.title.regular,
            fontSize = titleSize.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    start = titleStartPadding.dp,
                    end = titleEndPadding.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .align(BiasAlignment(-1f, titleVerticalBias))
        )

        trailingIcons?.let {
            Row(
                modifier = Modifier
                    .sizeIn(maxHeight = params.icons.maxSize.dp)
                    .align(BiasAlignment(1f, iconsVerticalBias))
                    .onSizeChanged { trailingIconsWidth = it.width },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                it()
            }
        }
    }
}

@Composable
private fun TrailingIconsExample() {
    val modifier = Modifier
        .clip(CircleShape)
        .clickable { }
        .padding(4.dp)

    Icon(
        Icons.Filled.Call,
        null,
        modifier = modifier
    )

    Icon(
        Icons.Filled.Build,
        null,
        modifier = modifier
    )

    Icon(
        Icons.Filled.AccountCircle,
        null,
        modifier = modifier
    )
}

@Composable
private fun LeadingIconExample() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { }
            .padding(4.dp)
    )
}
