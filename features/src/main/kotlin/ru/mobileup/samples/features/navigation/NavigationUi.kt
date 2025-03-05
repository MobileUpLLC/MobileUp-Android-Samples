package ru.mobileup.samples.features.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.message.presentation.noOverlapByMessage
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.navigation.NavigationComponent.Child.AlertDialogs
import ru.mobileup.samples.features.navigation.NavigationComponent.Child.BottomSheets
import ru.mobileup.samples.features.navigation.NavigationComponent.Child.Nested
import ru.mobileup.samples.features.navigation.alert_dialogs.NavigationAlertDialogsUi
import ru.mobileup.samples.features.navigation.bottom_sheets.NavigationBottomSheetsUi
import ru.mobileup.samples.features.navigation.nested.NavigationNestedUi
import ru.mobileup.samples.core.R as CoreR

@Composable
fun NavigationUi(
    component: NavigationComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.stack.collectAsState()
    val isBottomBarVisible by component.isBottomBarVisible.collectAsState()
    val selectedTab by component.selectedTab.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { paddingValues ->
            Children(stack) {
                when (val instance = it.instance) {
                    is AlertDialogs -> NavigationAlertDialogsUi(
                        component = instance.component,
                        paddingValues = paddingValues
                    )

                    is BottomSheets -> NavigationBottomSheetsUi(
                        component = instance.component,
                        paddingValues = paddingValues
                    )

                    is Nested -> NavigationNestedUi(
                        component = instance.component,
                        onBottomBarVisibilityChange = component::onBottomBarVisibilityChange,
                        paddingValues = paddingValues
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                TabBar(
                    selectedTab = selectedTab,
                    onTabSelect = component::onTabSelect,
                )
            }
        }
    )
}

@Composable
private fun TabBar(
    selectedTab: NavigationComponent.Tab,
    onTabSelect: (NavigationComponent.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabBarShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(tabBarShape)
            .border(
                border = BorderStroke(0.5.dp, CustomTheme.colors.border.primary),
                shape = tabBarShape
            )
            .background(CustomTheme.colors.background.screen)
            .navigationBarsPadding()
            .clickable(
                onClick = {},
                indication = null,
                interactionSource = remember(::MutableInteractionSource)
            )
            .noOverlapByMessage()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        ) {
            NavigationComponent.Tab.entries.forEach {
                BottomBarItem(
                    tab = it,
                    selectedTab = selectedTab,
                    onClick = onTabSelect,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: NavigationComponent.Tab,
    selectedTab: NavigationComponent.Tab,
    onClick: (NavigationComponent.Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = tab.nameForDisplay.localized()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            onClick = { onClick(tab) },
            indication = null,
            interactionSource = remember(::MutableInteractionSource)
        )
    ) {
        Icon(
            painter = painterResource(
                when (tab) {
                    NavigationComponent.Tab.Nested -> CoreR.drawable.ic_24_nested
                    NavigationComponent.Tab.AlertDialogs -> CoreR.drawable.ic_24_dialog
                    NavigationComponent.Tab.BottomSheets -> CoreR.drawable.ic_24_bottom_sheets
                }
            ),
            contentDescription = title
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (tab == selectedTab) {
                        CustomTheme.colors.button.primary.copy(alpha = 0.2f)
                    } else {
                        Color.Transparent
                    }
                )
        ) {
            Text(
                text = title,
                style = CustomTheme.typography.caption.regular,
                color = CustomTheme.colors.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(min = 36.dp)
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun NavigationUiPreview() {
    AppTheme {
        NavigationUi(FakeNavigationComponent())
    }
}
