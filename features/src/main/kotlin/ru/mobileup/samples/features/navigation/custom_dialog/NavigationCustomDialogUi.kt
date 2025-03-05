package ru.mobileup.samples.features.navigation.custom_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.core.R as CoreR

@Composable
fun NavigationCustomDialogUi(
    component: NavigationCustomDialogComponent,
    modifier: Modifier = Modifier
) {
    val name by component.name.collectAsState()
    val isSubmitting by component.isSubmitting.collectAsState()
    val isCloseButtonEnabled by component.isCloseButtonEnabled.collectAsState()

    Column(
        modifier = modifier
            .background(CustomTheme.colors.background.screen)
            .padding(20.dp)
    ) {
        Text(
            text = stringResource(R.string.navigation_custom_dialog),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(
            text = stringResource(
                R.string.navigation_custom_dialog_text,
                name,
            ),
            style = CustomTheme.typography.body.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppButton(
                text = stringResource(R.string.navigation_custom_dialog_submit),
                onClick = component::onSubmitClick,
                isLoading = isSubmitting,
                buttonType = ButtonType.Primary,
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = stringResource(CoreR.string.common_close),
                onClick = component::onCloseClick,
                isEnabled = isCloseButtonEnabled,
                buttonType = ButtonType.Secondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun NavigationCustomDialogUiPreview() {
    AppTheme {
        NavigationCustomDialogUi(FakeNavigationCustomDialogComponent())
    }
}
