package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.Dialog
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.collapsing_toolbar.domain.CollapsingToolbarLayoutType

@Composable
fun CollapsingToolbarMenuComponentUi(
    component: CollapsingToolbarMenuComponent,
    modifier: Modifier = Modifier
) {

    val isLazyLayout by component.isLazyLayout.collectAsState()
    val layoutType by component.layoutType.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(stringResource(R.string.collapsing_toolbar_lazy_layout))
                    Switch(
                        checked = isLazyLayout,
                        onCheckedChange = component::onLazyLayoutCheckedChange
                    )
                }

                IconButton(
                    onClick = component::onInfoClick,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.collapsing_toolbar_example),
                onClick = component::onExampleClick
            )

            Column(
                modifier = Modifier
                    .border(3.dp, Color.Blue, RoundedCornerShape(24.dp))
            ) {
                Text(
                    text = stringResource(R.string.collapsing_toolbar_layout_type),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )

                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_simple_normal,
                    selected = layoutType == CollapsingToolbarLayoutType.SimpleNormal,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.SimpleNormal) }
                )
                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_simple_custom,
                    selected = layoutType == CollapsingToolbarLayoutType.SimpleCustom,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.SimpleCustom) }
                )
                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_simple_both,
                    selected = layoutType == CollapsingToolbarLayoutType.SimpleBoth,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.SimpleBoth) }
                )

                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_advanced_normal,
                    selected = layoutType == CollapsingToolbarLayoutType.AdvancedNormal,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.AdvancedNormal) }
                )
                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_advanced_custom,
                    selected = layoutType == CollapsingToolbarLayoutType.AdvancedCustom,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.AdvancedCustom) }
                )
                LayoutTypeItem(
                    title = R.string.collapsing_toolbar_layout_type_advanced_both,
                    selected = layoutType == CollapsingToolbarLayoutType.AdvancedBoth,
                    onClick = { component.onLayoutTypeClicked(CollapsingToolbarLayoutType.AdvancedBoth) }
                )
            }
        }
    }

    Dialog(component.infoDialog) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Center
        ) {
            val text = buildString {
                append("Есть 2 способа создать CollapsingToolbar:")
                append("\n\n")
                append("1. С помощью Layout (назвал Advanced)\n")
                append("2. Стандартными вьюшками, анимируя color/size/padding etc через animateXasState/transition (назвал Simple)")
                append("\n\n\n")
                append("Есть 2 способа получить текущую позицию скролла")
                append("\n\n")
                append("1. rememberScrollState().value\n")
                append("2. через реализацию NestedScrollConnection")
                append("\n\n")
                append("Первый способ проще, второй не избежать при использовании lazy column/row. Дальнейшая реализация одинаковая, главное иметь current scroll value")
            }
            Text(
                text = text,
                modifier = Modifier
                    .padding(24.dp)
            )
        }
    }
}

@Composable
private fun LayoutTypeItem(
    @StringRes title: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            stringResource(title),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        RadioButton(
            selected = selected,
            onClick = onClick
        )
    }
}