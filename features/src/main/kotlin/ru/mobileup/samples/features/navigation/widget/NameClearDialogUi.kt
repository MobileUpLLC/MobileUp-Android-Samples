package ru.mobileup.samples.features.navigation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R

@Composable
fun NameClearDialogUi(
    name: String,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .background(CustomTheme.colors.background.screen)
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(R.string.navigation_simple_dialog_title),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary
        )
        Text(
            text = stringResource(R.string.navigation_simple_dialog_text, name),
            style = CustomTheme.typography.body.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        AppButton(
            text = stringResource(R.string.navigation_clear),
            buttonType = ButtonType.Primary,
            onClick = onClearClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}