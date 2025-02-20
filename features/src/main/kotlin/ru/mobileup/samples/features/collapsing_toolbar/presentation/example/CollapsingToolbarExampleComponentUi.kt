package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.AppBarNestedScrollConnection
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.CollapsingToolbarMockItem
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.CollapsingToolbarSimpleCustom
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.advanced_custom.CollapsingToolbarAdvancedCustom
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.advanced_normal.CollapsingToolbarAdvancedNormal
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.simple_normal.CollapsingToolbarSimpleNormal

@Composable
fun CollapsingToolbarExampleComponentUi(
    component: CollapsingToolbarExampleComponent,
    modifier: Modifier = Modifier
) {
    val nsc = remember { AppBarNestedScrollConnection() }
    val ss = rememberScrollState()
    val scrollValue by remember {
        derivedStateOf { if (component.isLazyLayout) nsc.appBarOffset else ss.value }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(component, scrollValue) }
    ) { innerPadding ->
        if (component.isLazyLayout) {
            LazyContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .nestedScroll(nsc)
            )
        } else {
            NormalContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(ss)
            )
        }
    }
}

@Composable
private fun TopBar(
    component: CollapsingToolbarExampleComponent,
    scrollValue: Int
) {
    when (component.type) {
        CollapsingToolbarLayoutType.SimpleNormal -> CollapsingToolbarSimpleNormal(
            scrollValue = scrollValue,
            modifier = Modifier
                .statusBarsPadding(),
        )
        CollapsingToolbarLayoutType.SimpleCustom -> CollapsingToolbarSimpleCustom(
            scrollValue,
            modifier = Modifier
                .statusBarsPadding()
        )
        CollapsingToolbarLayoutType.SimpleBoth -> Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            CollapsingToolbarSimpleCustom(
                scrollValue,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(6.dp)
                        .background(Color.Cyan, CircleShape)

                )
            }
            CollapsingToolbarSimpleNormal(
                scrollValue = scrollValue,
            )
        }
        CollapsingToolbarLayoutType.AdvancedNormal -> CollapsingToolbarAdvancedNormal(scrollValue)
        CollapsingToolbarLayoutType.AdvancedCustom -> CollapsingToolbarAdvancedCustom(scrollValue)
        CollapsingToolbarLayoutType.AdvancedBoth -> Column(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            CollapsingToolbarAdvancedNormal(scrollValue)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(6.dp)
                        .background(Color.Cyan, CircleShape)

                )
            }
            CollapsingToolbarAdvancedCustom(scrollValue)
        }
    }
}

@Composable
private fun LazyContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier,
        ) {
            items(50) {
                CollapsingToolbarMockItem(it)
            }
        }
    }
}

@Composable
private fun NormalContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        repeat(30) {
            CollapsingToolbarMockItem(it)
        }
    }
}