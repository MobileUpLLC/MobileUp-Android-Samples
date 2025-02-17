package ru.mobileup.samples.features.collapsing_toolbar.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.Dialog
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R

@Composable
fun CollapsingToolbarMenuUi(
    component: CollapsingToolbarMenuComponent,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppButton(
                buttonType = ButtonType.Primary,
                onClick = component::onInfoClick,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.collapsing_toolbar_info),
                    style = CustomTheme.typography.button.bold
                )
            }
        }

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.collapsing_toolbar_layout_type_common),
            onClick = component::onCommonClick
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.collapsing_toolbar_layout_type_custom),
            onClick = component::onCustomClick
        )
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
                append("1. С помощью Layout (назвал Custom)\n")
                append("2. Стандартными вьюшками, анимируя color/size/padding etc через animateXasState/transition (назвал Common)")
                append("\n\n\n")
                append("Есть 2 способа получить текущую позицию скролла")
                append("\n\n")
                append("1. rememberScrollState().value\n")
                append("2. через реализацию NestedScrollConnection")
                append("\n\n")
                append("С целью стандартизации будет использоваться AppBarNestedScrollConnection который реализует интерфейс NestedScrollConnection")
            }
            Text(
                text = text,
                modifier = Modifier
                    .padding(24.dp)
            )
        }
    }
}
