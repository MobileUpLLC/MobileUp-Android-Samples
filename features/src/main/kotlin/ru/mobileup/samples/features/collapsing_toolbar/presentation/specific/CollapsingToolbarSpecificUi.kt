package ru.mobileup.samples.features.collapsing_toolbar.presentation.specific

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.window.core.layout.WindowWidthSizeClass
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.features.collapsing_toolbar.presentation.common.widget.CustomToolbarDefaults
import ru.mobileup.samples.features.collapsing_toolbar.presentation.specific.widget.SpecificToolbar

@Composable
fun CollapsingToolbarSpecificUi(
    modifier: Modifier = Modifier,
) {
    var canCollapse by remember { mutableStateOf(true) }

    var convergenceCoefficient by remember { mutableFloatStateOf(0f) }

    var itemsCount by remember { mutableIntStateOf(10) }

    val scrollState = rememberScrollState()

    val scrollBehavior = CustomToolbarDefaults.customToolbarScrollBehavior(
        canCollapse = { canCollapse },
        convergenceCoefficient = { convergenceCoefficient }
    )

    val changeSystemBarColor = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    if (changeSystemBarColor) {
        SystemBars(
            statusBarColor = Color.Transparent,
            statusBarIconsColor = SystemBarIconsColor.Light,
        )
    }

    // Don't copy ⚠️ For demonstration purposes, the ScrollState and ScrollBehavior positions are reset.
    LaunchedEffect(canCollapse, convergenceCoefficient, itemsCount) {
        scrollState.scrollTo(0)
        scrollBehavior.state.heightOffset = 0f
        scrollBehavior.state.contentOffset = 0f
    }

    var settingsPopupShow by remember {
       mutableStateOf(false)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SpecificToolbar(scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { settingsPopupShow = !settingsPopupShow }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(itemsCount) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CustomTheme.colors.background.secondary)
                )
            }
        }

        if (settingsPopupShow) {
            Popup(
                offset = IntOffset(x = 30, y = 30),
                alignment = Alignment.BottomStart,
                onDismissRequest = { settingsPopupShow = false }
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(min = 250.dp),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        Text("Can collapse")
                        Switch(
                            checked = canCollapse,
                            onCheckedChange = { canCollapse = !canCollapse }
                        )

                        Text(
                            text = "Convergence Coefficient: ${String.format(Locale.current.platformLocale, "%.2f", convergenceCoefficient)}"
                        )
                        Slider(
                            modifier = Modifier
                                .width(250.dp),
                            value = convergenceCoefficient,
                            onValueChange = { convergenceCoefficient = it }
                        )

                        Text(text = "Items count")
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .clickableNoRipple {
                                        itemsCount = (itemsCount + 1).coerceAtMost(15)
                                    },
                                text = "+",
                                fontSize = 22.sp
                            )
                            Text(text = "$itemsCount")
                            Text(
                                modifier = Modifier
                                    .clickableNoRipple {
                                        itemsCount = (itemsCount - 1).coerceAtLeast(0)
                                    },
                                text = "-",
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
