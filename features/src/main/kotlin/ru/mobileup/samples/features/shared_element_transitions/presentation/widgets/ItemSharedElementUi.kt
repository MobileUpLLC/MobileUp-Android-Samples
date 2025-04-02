package ru.mobileup.samples.features.shared_element_transitions.presentation.widgets

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.ScaleToBounds
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.LocalSharedScope
import ru.mobileup.samples.core.widget.SharedScope.Companion.sharedScopeModifier
import ru.mobileup.samples.features.shared_element_transitions.data.ItemSharedElementItems
import ru.mobileup.samples.features.shared_element_transitions.domain.ItemSharedElement

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ItemSharedElementUi(
    item: ItemSharedElement,
    onClick: (ItemSharedElement) -> Unit,
    modifier: Modifier = Modifier,
) {
    val localSharedScope = LocalSharedScope.current

    val context = LocalContext.current

    val cardRoundedCornerAnimation = localSharedScope?.animatedVisibilityScope?.transition
        ?.animateDp(label = "card_rounded_corner") { enterExit ->
            when (enterExit) {
                EnterExitState.PreEnter -> 0.dp
                EnterExitState.Visible -> 16.dp
                EnterExitState.PostExit -> 16.dp
            }
        }

    val cardShape by remember(cardRoundedCornerAnimation?.value) {
        mutableStateOf(
            cardRoundedCornerAnimation?.let {
                RoundedCornerShape(it.value)
            } ?: RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = cardShape)
            .clip(cardShape)
            .background(CustomTheme.colors.background.screen)
            .clickable { onClick(item) }
            .sharedScopeModifier { animScope ->
                Modifier.sharedBounds(
                    rememberSharedContentState(key = SharedKeys.surface(item.id)),
                    animatedVisibilityScope = animScope,
                    clipInOverlayDuringTransition = OverlayClip(cardShape),
                    renderInOverlayDuringTransition = false,
                )
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(75.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .align(Alignment.Top)
                    .sharedScopeModifier { animScope ->
                        Modifier.sharedBounds(
                            rememberSharedContentState(key = SharedKeys.image(item.id)),
                            animatedVisibilityScope = animScope,
                            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp)),
                            boundsTransform = { _, _ ->
                                spring(
                                    dampingRatio = 0.8f,
                                    stiffness = 380f
                                )
                            },
                        )
                    },
                model = ImageRequest.Builder(context)
                    .crossfade(true)
                    .data(item.image.uri)
                    .placeholderMemoryCacheKey("shared-image-key-${item.id}")
                    .memoryCacheKey("shared-image-key-${item.id}")
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Row(
                modifier = Modifier
                    .align(Alignment.Top)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .weight(1f)
                        .sharedScopeModifier { animScope ->
                            Modifier.sharedBounds(
                                rememberSharedContentState(key = SharedKeys.title(item.id)),
                                animatedVisibilityScope = animScope,
                                zIndexInOverlay = 1f,
                            )
                        },
                    text = item.title,
                    style = CustomTheme.typography.body.regular,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .sharedScopeModifier { animScope ->
                            Modifier.sharedElement(
                                rememberSharedContentState(key = SharedKeys.shareButton(item.id)),
                                animatedVisibilityScope = animScope,
                            )
                        },
                    tint = Color.Black
                )
            }
        }

        Text(
            modifier = Modifier
                .sharedScopeModifier { animScope ->
                    Modifier.sharedBounds(
                        rememberSharedContentState(key = SharedKeys.text(item.id)),
                        animatedVisibilityScope = animScope,
                        resizeMode = ScaleToBounds(ContentScale.FillWidth, Alignment.TopStart),
                    )
                },
            text = item.text,
            style = CustomTheme.typography.body.regular,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun PreviewItemSharedElementUi() {
    AppTheme {
        ItemSharedElementUi(
            item = ItemSharedElementItems.items.first(),
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}