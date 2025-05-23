package ru.mobileup.samples.features.multipane_menu.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.multipane_menu.presentation.details.MultiPaneDetailsUi
import ru.mobileup.samples.features.multipane_menu.presentation.list.MultiPaneMenuUi
import ru.mobileup.samples.core.R as CoreR

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MultiPaneUi(
    component: MultiPaneComponent,
    modifier: Modifier = Modifier,
) {
    val panels by component.panels.collectAsState()

    val isCompact =
        currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val mode = if (isCompact) ChildPanelsMode.SINGLE else ChildPanelsMode.DUAL

    var showInfo by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(mode) {
        component.setMode(mode)
    }

    Column(
        modifier = modifier.statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { showInfo = !showInfo }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = CustomTheme.colors.icon.primary
                )
            }

            if (mode == ChildPanelsMode.DUAL) {
                IconButton(
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
                MultiPaneMenuUi(
                    modifier = Modifier.background(CustomTheme.colors.background.screen),
                    component = it.instance,
                    mode = mode
                )
            },
            detailsChild = {
                MultiPaneDetailsUi(
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

    MultiPaneInfoDialog(
        showDialog = showInfo,
        onDismiss = { showInfo = false }
    )
}

@Composable
private fun MultiPaneInfoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = showDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SystemBars(
            statusBarColor = Color.Transparent,
            navigationBarColor = Color.Transparent,
            statusBarIconsColor = SystemBarIconsColor.Light,
            navigationBarIconsColor = SystemBarIconsColor.Light
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .widthIn(min = 280.dp, max = 560.dp),
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(R.string.menu_multipane_info_description))
                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = onDismiss
                    ) {
                        Text(stringResource(CoreR.string.common_ok))
                    }
                }
            }
        }
    }
}
