package ru.mobileup.samples.features.image.presentation.carousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.zoomable

@Composable
fun FullScreenImageCarouselUi(
    component: ImageCarouselComponent,
    modifier: Modifier = Modifier,
    minScale: Float = 1f,
    maxScale: Float = 3f,
) {
    val imageCarousel by component.imageCarousel.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = { imageCarousel.imageResources.size },
        initialPage = imageCarousel.currentImagePosition
    )

    var scrollEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            component.onCarouselPageChanged(page)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier.matchParentSize(),
            state = pagerState,
            userScrollEnabled = scrollEnabled
        ) {
            Column {
                AsyncImage(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .zoomable(
                            minScale,
                            maxScale,
                            onEndOfContentReached = { scrollEnabled = it }
                        ),
                    model = imageCarousel.imageResources.getOrNull(it)?.uri,
                    contentDescription = null
                )
            }
        }

        if (imageCarousel.imageResources.size > 1) {
            PageIndicator(
                imagesCount = imageCarousel.imageResources.size,
                pagerState = pagerState,
                defaultColor = CustomTheme.colors.background.secondary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ImageViewerUi() {
    AppTheme {
        FullScreenImageCarouselUi(FakeImageCarouselComponent())
    }
}