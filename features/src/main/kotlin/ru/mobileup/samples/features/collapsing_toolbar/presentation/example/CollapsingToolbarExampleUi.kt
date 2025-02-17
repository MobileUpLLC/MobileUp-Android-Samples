package ru.mobileup.samples.features.collapsing_toolbar.presentation.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.AppBarNestedScrollConnection
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.CollapsingToolbarMockItem
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common.CollapsingToolbarCommon
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.common_multiline.CollapsingToolbarMultiline
import ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.custom.CollapsingToolbarCustom

@Composable
fun CollapsingToolbarExampleUi(
    component: CollapsingToolbarExampleComponent,
    modifier: Modifier = Modifier
) {
    val nsc = remember { AppBarNestedScrollConnection() }
    val scrollValue by remember {
        derivedStateOf { nsc.appBarOffset }
    }

    if (!component.isCommon) {
        SystemBars(lightStatusBarIcons = true)
    }

    Scaffold(
        modifier = modifier.nestedScroll(nsc),
        topBar = {
            TopBar(
                isCommon = component.isCommon,
                scrollValue = scrollValue
            )
        },
    ) { innerPadding ->
        Content(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun TopBar(
    isCommon: Boolean,
    scrollValue: Int
) {
    if (isCommon) {
        CollapsingToolbarCommon(
            scrollValue = scrollValue,
            modifier = Modifier
                .statusBarsPadding(),
        )
    } else {
        CollapsingToolbarCustom(scrollValue)
    }
}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        repeat(3) {
            CollapsingToolbarMockItem(it)
        }
    }
}
