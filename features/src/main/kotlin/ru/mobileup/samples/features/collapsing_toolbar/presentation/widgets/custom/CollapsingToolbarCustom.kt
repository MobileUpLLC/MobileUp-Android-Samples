package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import ru.mobileup.samples.core.utils.dispatchOnBackPressed
import ru.mobileup.samples.features.R
import kotlin.math.roundToInt

@Composable
fun CollapsingToolbarCustom(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    leadingIconImageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
) {
    val ctx = LocalContext.current
    val params = remember { CollapsingToolbarCustomParams() }
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val limitedScrollValue =
        remember(scrollValue) { scrollValue.coerceAtMost(params.collapsedOffset).toFloat() }
    val scrollFraction = remember(limitedScrollValue) { limitedScrollValue / params.collapsedOffset }

    val containerHeight by animateFloatAsState(
        lerp(params.container.maxHeight, params.container.minHeight, scrollFraction)
    )
    val backgroundAlpha by animateFloatAsState(
        lerp(1f, 0f, scrollFraction)
    )
    val isCollapsed by remember(scrollFraction) {
        mutableStateOf(scrollFraction == 1f)
    }

    Layout(
        modifier = modifier
            .height(containerHeight.dp),
        content = {
            Background(
                imageId = params.background.imageId,
                gradientId = params.background.gradientId,
                backgroundAlpha = backgroundAlpha
            )
            LeadingIcon(
                id = params.leadingIcon.id,
                leadingIconImageVector = leadingIconImageVector,
                onLeadingIconClick = { dispatchOnBackPressed(ctx) }
            )
            PlayerNumber(
                id = params.player.number.id,
                isCollapsed = isCollapsed
            )
            PlayerPhoto(
                id = params.player.photo.id,
                isCollapsed = isCollapsed
            )
            PlayerName(
                id = params.player.name.id,
                isCollapsed = isCollapsed
            )
        }
    ) { measures, constraints ->
        content(params, measures, constraints, statusBarPadding, scrollFraction)
    }
}

private fun MeasureScope.content(
    params: CollapsingToolbarCustomParams,
    measures: List<Measurable>,
    constraints: Constraints,
    statusBarPadding: PaddingValues,
    scrollFraction: Float
): MeasureResult {
    val bgImagePlaceable =
        measures.findMeasurableById(params.background.imageId)?.measure(constraints)
    val bgGradientPlaceable =
        measures.findMeasurableById(params.background.gradientId)?.measure(constraints)
    val leadingIconPlaceable = getLeadingIconPlaceable(params.leadingIcon, measures)
    val playerNumberPlaceable =
        measures.findMeasurableById(params.player.number.id)?.measure(Constraints())
    val playerPhotoPlaceable = getPlayerPhotoPlaceable(params.player.photo, measures, scrollFraction)
    val playerNamePlaceable = measures.findMeasurableById(params.player.name.id)?.measure(Constraints())

    return layout(constraints.maxWidth, constraints.maxHeight) {
        bgImagePlaceable?.place(0, 0, params.background.imageZIndex)
        bgGradientPlaceable?.place(0, 0, params.background.gradientZIndex)
        leadingIconPlaceable?.place(
            x = (params.leadingIcon.paddingStart * density).roundToInt(),
            y = statusBarPadding.calculateTopPadding().roundToPx(),
            zIndex = params.contextZIndex
        )
        /**
         * player photo, name, number centerVertically alignment
         */
        val maxHeight = listOf(playerPhotoPlaceable, playerNamePlaceable, playerNumberPlaceable)
            .maxOf { it?.height ?: 0 }
        placePlayerNumber(
            extraYOffset = getPlayerInfoExtraYOffset(
                maxHeight,
                height = playerNumberPlaceable?.height,
                paddingBottom = params.player.bottomPadding,
                density = density
            ),
            playerNumberPlaceable = playerNumberPlaceable,
            constraints = constraints,
            density = density,
            zIndex = params.contextZIndex,
            padding = params.player.number.padding
        )
        placePlayerPhoto(
            playerPhotoPlaceable = playerPhotoPlaceable,
            constraints = constraints,
            density = density,
            fraction = scrollFraction,
            extraYOffset = getPlayerInfoExtraYOffset(
                maxHeight = maxHeight,
                height = playerPhotoPlaceable?.height,
                paddingBottom = params.player.bottomPadding,
                density = density,
            ),
            padding = params.player.photo.maxPaddingStart,
            zIndex = params.contextZIndex,
            paddingStartFraction = params.player.photo.bottomPaddingStartFraction
        )
        placePlayerName(
            playerNamePlaceable = playerNamePlaceable,
            xOffset = playerPhotoPlaceable?.width?.plus((params.player.name.paddingStart * density))?.roundToInt() ?: 0,
            extraYOffset = getPlayerInfoExtraYOffset(
                maxHeight = maxHeight,
                height = playerNamePlaceable?.height,
                paddingBottom = params.player.bottomPadding,
                density = density
            ),
            constraints = constraints,
            zIndex = params.contextZIndex
        )
    }
}

private fun List<Measurable>.findMeasurableById(layoutId: Any): Measurable? = find {
    it.layoutId == layoutId
}

private fun MeasureScope.getPlayerPhotoPlaceable(
    params: CollapsingToolbarCustomParams.Player.Photo,
    measures: List<Measurable>,
    scrollFraction: Float
): Placeable? {
    val width = lerp(params.maxWidth, params.minSize, scrollFraction)
        .times(density).roundToInt()
    val height = lerp(params.maxHeight, params.minSize, scrollFraction)
        .times(density).roundToInt()
    return measures.find { it.layoutId == params.id }?.measure(
        Constraints(maxWidth = width, maxHeight = height, minHeight = height, minWidth = width)
    )
}

@Composable
private fun PlayerName(
    id: Any,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isCollapsed,
        modifier = modifier
            .layoutId(id)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = "Donald",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Trump",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun PlayerPhoto(
    id: Any,
    isCollapsed: Boolean,
    modifier: Modifier = Modifier
) {
    val collapsedPhotoModifier = if (isCollapsed) {
        modifier
            .background(Color.White, CircleShape)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = CircleShape
            )
            .clip(CircleShape)
    } else {
        modifier
    }

    Image(
        painter = painterResource(R.drawable.ic_football_player),
        contentDescription = null,
        modifier = collapsedPhotoModifier
            .layoutId(id)
    )
}

@Composable
private fun PlayerNumber(
    id: Any,
    isCollapsed: Boolean
) {
    AnimatedVisibility(
        visible = isCollapsed,
        modifier = Modifier
            .layoutId(id)
    ) {
        Text(
            text = "17",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
private fun LeadingIcon(
    id: Any,
    leadingIconImageVector: ImageVector,
    onLeadingIconClick: () -> Unit
) {
    Icon(
        imageVector = leadingIconImageVector,
        contentDescription = null,
        tint = Color.White,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onLeadingIconClick)
            .layoutId(id)
    )
}

@Composable
private fun Background(
    imageId: Any,
    gradientId: Any,
    backgroundAlpha: Float
) {
    Image(
        painter = painterResource(R.drawable.ic_football_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(backgroundAlpha)
            .background(Color.Green)
            .layoutId(imageId)
    )
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .alpha(1 - backgroundAlpha)
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF021735), Color(0xFFEB0A33))
                )
            )
            .layoutId(gradientId),
    )
}

private fun Placeable.PlacementScope.placePlayerName(
    zIndex: Float,
    extraYOffset: Int,
    xOffset: Int,
    playerNamePlaceable: Placeable?,
    constraints: Constraints,
) {
    playerNamePlaceable?.place(
        x = xOffset,
        y = constraints.maxHeight - extraYOffset - playerNamePlaceable.height,
        zIndex = zIndex
    )
}

private fun getPlayerInfoExtraYOffset(
    maxHeight: Int,
    height: Int?,
    paddingBottom: Int,
    density: Float
): Int =
    (maxHeight - (height ?: 0)).div(2)
        .plus(paddingBottom.times(density).roundToInt())

private fun Placeable.PlacementScope.placePlayerNumber(
    extraYOffset: Int,
    playerNumberPlaceable: Placeable?,
    constraints: Constraints,
    density: Float,
    zIndex: Float,
    padding: Int
) {
    playerNumberPlaceable?.place(
        x = constraints.maxWidth - (padding * density).roundToInt() - playerNumberPlaceable.width,
        y = constraints.maxHeight - extraYOffset - playerNumberPlaceable.height,
        zIndex = zIndex
    )
}

private fun Placeable.PlacementScope.placePlayerPhoto(
    playerPhotoPlaceable: Placeable?,
    extraYOffset: Int,
    constraints: Constraints,
    density: Float,
    fraction: Float,
    padding: Int,
    zIndex: Float,
    paddingStartFraction: Float
) {
    val photoWidthCoefficient = lerp(0.5f, 0f, fraction)
    val photoWidthOffsetX = playerPhotoPlaceable?.width?.times(photoWidthCoefficient)?.roundToInt() ?: 0
    val photoHeight = playerPhotoPlaceable?.height ?: 0
    val offsetX = ((constraints.maxWidth / 2) * (1f - fraction)).roundToInt()
    val startPadding = (padding * density * fraction).roundToInt()
    val bottomPadding = (
        extraYOffset * getPlayerPhotoBottomPaddingFraction(fraction, paddingStartFraction)
        ).roundToInt()
    playerPhotoPlaceable?.place(
        x = offsetX - photoWidthOffsetX + startPadding,
        y = constraints.maxHeight - photoHeight - bottomPadding,
        zIndex = zIndex
    )
}

private fun getPlayerPhotoBottomPaddingFraction(fraction: Float, startFraction: Float): Float {
    if (fraction < startFraction) return 0f
    return (fraction - startFraction) / (1f - startFraction)
}

private fun MeasureScope.getLeadingIconPlaceable(
    params: CollapsingToolbarCustomParams.LeadingIcon,
    measures: List<Measurable>
) =
    measures.find { it.layoutId == params.id }
        ?.measure(
            Constraints(
                minWidth = (params.size * density).roundToInt(),
                minHeight = (params.size * density).roundToInt()
            )
        )
