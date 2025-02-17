package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common_multiline

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mobileup.samples.core.theme.custom.CustomTheme

@Composable
fun CollapsingToolbarMultiline(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    title: String = "Раз два три четыре пять шесть семь восемь девять десять",
    maxLines: Int = Int.MAX_VALUE,
    leadingIcon: @Composable (BoxScope.() -> Unit)? = { LeadingIconExample() },
    trailingIcons: @Composable (RowScope.() -> Unit)? = { TrailingIconsExample() }
) {
    val density = LocalDensity.current

    var leadingIconSize by remember { mutableStateOf(IntSize.Zero) }
    var trailingIconsSize by remember { mutableStateOf(IntSize.Zero) }
    var collapsedTitleSize by remember { mutableStateOf(IntSize.Zero) }
    var expandedTitleSize by remember { mutableStateOf(IntSize.Zero) }
    var maxContainerHeight by remember { mutableIntStateOf(Int.MAX_VALUE) }

    val params = remember(scrollValue, leadingIconSize, trailingIconsSize, collapsedTitleSize) {
        CollapsingToolbarMultilineParams(
            scrollValue = scrollValue,
            leadingIconSize = leadingIconSize,
            trailingIconsSize = trailingIconsSize,
            collapsedTitleSize = collapsedTitleSize,
            expandedTitleSize = expandedTitleSize,
            maxContainerHeight = maxContainerHeight,
            density = density.density
        )
    }

    val leadingIconTopPadding by animateFloatAsState(params.icons.leadingIconTopPadding)
    val trailingIconsTopPadding by animateFloatAsState(params.icons.trailingIconsTopPadding)

    val expandedTitleScale by animateFloatAsState(params.expandedTitle.scale)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = params.container.minHeight.dp, max = params.container.maxHeight.dp)
            .padding(horizontal = 16.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height - 4 * density.density),
                    end = Offset(size.width, size.height - 4 * density.density)
                )
            }
            .onSizeChanged {
                if (it.height > maxContainerHeight || maxContainerHeight == Int.MAX_VALUE) {
                    maxContainerHeight = it.height
                }
            }
            .animateContentSize()
    ) {
        leadingIcon?.let {
            Box(
                modifier = Modifier
                    .padding(top = leadingIconTopPadding.dp)
                    .sizeIn(maxHeight = params.icons.maxSize.dp, maxWidth = params.icons.maxSize.dp)
                    .align(Alignment.TopStart)
                    .onSizeChanged { leadingIconSize = it },
                contentAlignment = Alignment.Center
            ) {
                it()
            }
        }

        // collapsed
        Text(
            text = title,
            style = CustomTheme.typography.title.regular,
            fontSize = params.collapsedTitle.fontSize.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(
                    top = params.collapsedTitle.topPadding.dp,
                    start = params.collapsedTitle.startPadding.dp,
                    end = params.collapsedTitle.endPadding.dp,
                )
                .alpha(params.collapsedTitle.alpha)
                .onSizeChanged { collapsedTitleSize = it }
        )
        // collapsed

        // expanded
        Column {
            Text(
                text = title,
                style = CustomTheme.typography.title.regular,
                fontSize = params.expandedTitle.fontSize.sp,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .alpha(params.expandedTitle.alpha)
                    .scale(expandedTitleScale)
                    .graphicsLayer {
                        scaleY = expandedTitleScale
                        scaleX = expandedTitleScale
                        translationY = params.expandedTitle.expandedTopOffset
                    }
                    .onSizeChanged { expandedTitleSize = it }
            )

            Spacer(Modifier.height(params.expandedTitle.expandedTopOffset.dp))
        }
        // expanded

        trailingIcons?.let {
            Row(
                modifier = Modifier
                    .padding(top = trailingIconsTopPadding.dp)
                    .onSizeChanged { trailingIconsSize = it }
                    .sizeIn(maxHeight = params.icons.maxSize.dp)
                    .align(Alignment.TopEnd),
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
