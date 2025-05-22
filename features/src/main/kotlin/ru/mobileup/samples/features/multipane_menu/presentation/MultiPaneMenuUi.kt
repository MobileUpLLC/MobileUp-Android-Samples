package ru.mobileup.samples.features.multipane_menu.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanels
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanelsAnimators
import com.arkivanov.decompose.extensions.compose.experimental.panels.HorizontalChildPanelsLayout
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.multipane_menu.presentation.details.SampleDetailsUi
import ru.mobileup.samples.features.multipane_menu.presentation.list.SampleListUi

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MultiPaneMenuUi(
    component: MultiPaneMenuComponent,
    modifier: Modifier = Modifier,
) {
    val panels by component.panels.collectAsState()

    val isCompact =
        currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val mode = if (isCompact) ChildPanelsMode.SINGLE else ChildPanelsMode.DUAL

    LaunchedEffect(mode) {
        component.setMode(mode)
    }

    Column(
        modifier = modifier.run {
            if (mode == ChildPanelsMode.DUAL) statusBarsPadding() else this
        }
    ) {
        if (mode == ChildPanelsMode.DUAL) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = component::onSettingsClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = CustomTheme.colors.icon.primary
                    )
                }
            }
        }

        ChildPanels(
            panels = panels,
            mainChild = {
                SampleListUi(
                    modifier = Modifier.background(CustomTheme.colors.background.screen),
                    component = it.instance,
                    mode = mode
                )
            },
            detailsChild = {
                SampleDetailsUi(
                    modifier = Modifier.background(CustomTheme.colors.background.screen),
                    component = it.instance
                )
            },
            layout = HorizontalChildPanelsLayout(
                dualWeights = Pair(first = 0.4F, second = 0.6F),
            ),
            secondPanelPlaceholder = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.menu_item_details_placeholder))
                }
            },
            animators = ChildPanelsAnimators(single = slide(), dual = fade() to fade()),
            predictiveBackParams = {
                PredictiveBackParams(
                    backHandler = component.backHandler,
                    onBack = component::onBackClick,
                    animatable = ::materialPredictiveBackAnimatable,
                )
            },
        )
    }
}
