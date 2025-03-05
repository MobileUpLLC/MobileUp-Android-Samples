package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.video.data.render.availableFilters
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@Composable
fun BoxScope.PlayerFilterSelector(
    playerConfig: PlayerConfig,
    filtersPagerState: PagerState,
    fling: TargetedFlingBehavior,
    modifier: Modifier = Modifier
) {
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = playerConfig == PlayerConfig.Filter
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomTheme.colors.palette.black50),
            pageSize = PageSize.Fill,
            state = filtersPagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            flingBehavior = fling,
            pageSpacing = 7.dp,
            key = { it },
        ) { page ->
            val filterIndex by remember { derivedStateOf { page % availableFilters.size } }
            val filter by remember { mutableStateOf(availableFilters[filterIndex]) }
            val isActive by remember {
                derivedStateOf { filtersPagerState.currentPage == page }
            }

            Text(
                text = filter.name,
                color = if (isActive) {
                    CustomTheme.colors.text.warning
                } else {
                    CustomTheme.colors.text.invert
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PlayerFilterSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            val filtersPagerState = rememberPagerState(
                initialPage = 0,
            ) { availableFilters.size }

            val fling = PagerDefaults.flingBehavior(
                state = filtersPagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(15)
            )
            PlayerFilterSelector(
                playerConfig = PlayerConfig.Filter,
                filtersPagerState = filtersPagerState,
                fling = fling
            )
        }
    }
}