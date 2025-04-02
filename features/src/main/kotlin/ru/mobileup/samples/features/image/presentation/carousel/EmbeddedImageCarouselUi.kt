package ru.mobileup.samples.features.image.presentation.carousel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.mobileup.samples.features.image.domain.ImageCarousel

@Composable
fun EmbeddedImageCarouselUi(
    component: ImageCarouselComponent,
    modifier: Modifier = Modifier
) {

    val imageCarousel by component.imageCarousel.collectAsState()

    val pagerState = rememberPagerState(
        pageCount = { imageCarousel.imageResources.size },
        initialPage = imageCarousel.currentImagePosition
    )

    Box(modifier = modifier) {
        ImagePager(
            pagerState = pagerState,
            imageCarousel = imageCarousel,
            onPageChange = component::onCarouselPageChanged,
            onImageClick = component::onImageClick
        )

        ImageOverlay(
            modifier = Modifier.matchParentSize(),
            imagesCount = imageCarousel.imageResources.size,
            pagerState = pagerState,
        )
    }
}

@Composable
private fun ImagePager(
    pagerState: PagerState,
    imageCarousel: ImageCarousel,
    onPageChange: (Int) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState, onPageChange) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPageChange(page)
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) {
        val imageUrl = imageCarousel.imageResources.getOrNull(it)
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8333f)
                .clickable(onClick = onImageClick),
            model = imageUrl?.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ImageOverlay(
    imagesCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        PageIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            imagesCount = imagesCount,
            pagerState = pagerState
        )
    }
}