package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.advanced_normal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.utils.dispatchOnBackPressed
import ru.mobileup.samples.core.utils.statusBarsPaddingDp
import ru.mobileup.samples.features.R

@Composable
fun CollapsingToolbarAdvancedNormal(
    scrollValue: Int,
    modifier: Modifier = Modifier,
    leadingIconImageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
) {
    val ctx = LocalContext.current
    val params = remember(scrollValue) {
        AdvancedNormalToolbarParams(scrollValue)
    }
    val transitionParams = updateTransition(params)

    val containerHeight by transitionParams.animateDp { it.container.height.dp }
    val backgroundAlpha by transitionParams.animateFloat { it.background.alpha }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
    ) {
        Background(alpha = backgroundAlpha)

        Player(
            transitionParams = transitionParams,
        )

        Icon(
            imageVector = leadingIconImageVector,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .clip(CircleShape)
                .clickable(onClick = { dispatchOnBackPressed(ctx) })
                .padding(start = 20.dp, top = statusBarsPaddingDp())
                .size(24.dp)
        )
    }
}

@Composable
private fun BoxScope.Player(
    transitionParams: Transition<AdvancedNormalToolbarParams>,
) {
    val verticalBias by transitionParams.animateFloat { it.playerInfo.verticalBias }
    val horizontalBias by transitionParams.animateFloat { it.playerInfo.horizontalBias }
    val width by transitionParams.animateDp { it.playerInfo.photoWidth.dp }
    val height by transitionParams.animateDp { it.playerInfo.photoHeight.dp }

    Row(
        modifier = Modifier
            .align(BiasAlignment(horizontalBias, verticalBias)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val collapsedPhotoModifier = if (transitionParams.targetState.playerInfo.showCollapsedElements) {
            Modifier
                .background(Color.White, CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = CircleShape
                )
                .clip(CircleShape)
        } else {
            Modifier
        }

        Image(
            painter = painterResource(R.drawable.ic_football_player),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = width,
                    height = height
                )
                .then(collapsedPhotoModifier),
        )

        transitionParams.AnimatedVisibility(
            visible = { it.playerInfo.showCollapsedElements }
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
    val playerNumberVerticalBias = transitionParams.currentState.playerInfo.playerNumberVerticalBias
    transitionParams.AnimatedVisibility(
        visible = { it.playerInfo.showCollapsedElements },
        modifier = Modifier
            .align(BiasAlignment(1f, playerNumberVerticalBias))
    ) {
        Text(
            text = "17",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(end = 20.dp)
        )
    }
}

@Composable
private fun Background(
    alpha: Float,
) {
    Image(
        painter = painterResource(R.drawable.ic_football_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(Color.Green),
    )
    Spacer(
        modifier = Modifier
            .fillMaxSize()
            .alpha(1 - alpha)
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFF021735), Color(0xFFEB0A33))
                )
            ),
    )
}
