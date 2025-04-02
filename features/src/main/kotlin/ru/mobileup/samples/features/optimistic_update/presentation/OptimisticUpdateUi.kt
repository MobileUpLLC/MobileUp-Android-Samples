package ru.mobileup.samples.features.optimistic_update.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.BottomSheet
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.LceWidget
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.presentation.server.OptimisticUpdateServerUi

@Composable
fun OptimisticUpdateUi(
    component: OptimisticUpdateComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.optimistic_update_client_title),
            textAlign = TextAlign.Center,
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(horizontal = 20.dp)
        )

        AppButton(
            onClick = component::onServerShowClick,
            buttonType = ButtonType.Primary,
            text = stringResource(R.string.optimistic_update_client_show_server),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        AppButton(
            onClick = component::onRefresh,
            buttonType = ButtonType.Primary,
            text = stringResource(R.string.optimistic_update_client_refresh),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        LceWidget(
            state = state,
            onRetryClick = component::onRefresh,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { data, _ ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.optimistic_update_client_palette_size,
                        data.paletteSize
                    ),
                    style = CustomTheme.typography.caption.regular,
                    color = CustomTheme.colors.text.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    OptimisticUpdateComponent.Tab.entries.forEach {
                        OptimisticTab(
                            tabText = stringResource(it.resId),
                            isSelected = data.selectedTab == it,
                            onClick = { component.onTabClick(it) }
                        )
                    }
                }

                when (data.selectedTab) {
                    OptimisticUpdateComponent.Tab.AllColors -> ColorCardList(
                        colors = data.allColors,
                        onAction = component::onAddColorClick,
                        actionText = stringResource(R.string.optimistic_update_client_add_color)
                    )

                    OptimisticUpdateComponent.Tab.Palette -> ColorCardList(
                        colors = data.palette,
                        onAction = component::onRemoveColorClick,
                        actionText = stringResource(R.string.optimistic_update_client_remove_color)
                    )
                }
            }
        }
    }

    BottomSheet(component.serverDialog) {
        OptimisticUpdateServerUi(it)
    }
}

@Composable
private fun OptimisticTab(
    tabText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (textColor, backgroundColor) = with(CustomTheme.colors) {
        when (isSelected) {
            true -> text.invert to button.primary
            false -> text.primary to button.secondary
        }
    }

    Text(
        text = tabText,
        color = textColor,
        modifier = Modifier
            .background(backgroundColor)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

@Composable
private fun ColorCardList(
    colors: List<PaletteColor>,
    onAction: (color: PaletteColor) -> Unit,
    actionText: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        itemsIndexed(colors) { index, item ->
            Surface(
                shadowElevation = 12.dp,
                shape = RoundedCornerShape(12.dp),
                color = CustomTheme.colors.background.screen
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .background(Color(item.value))
                    )
                    AppButton(
                        onClick = { onAction(item) },
                        buttonType = ButtonType.Secondary,
                        text = actionText,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}