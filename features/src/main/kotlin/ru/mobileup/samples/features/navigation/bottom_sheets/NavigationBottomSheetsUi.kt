package ru.mobileup.samples.features.navigation.bottom_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.dialog.BottomSheet
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.core.widget.text_field.AppTextField
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.navigation.custom_dialog.NavigationCustomDialogUi
import ru.mobileup.samples.features.navigation.widget.NameClearDialogUi

@Composable
fun NavigationBottomSheetsUi(
    component: NavigationBottomSheetsComponent,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    val name by component.nameInputControl.text.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(32.dp)
            .padding(paddingValues)
    ) {
        Text(
            text = stringResource(R.string.navigation_bottom_sheets),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary
        )
        AppTextField(
            inputControl = component.nameInputControl,
            placeholder = stringResource(R.string.navigation_bottom_sheets_name_placeholder),
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            buttonType = ButtonType.Secondary,
            onClick = component::onShowSimpleDialogControlClick,
            text = stringResource(R.string.navigation_bottom_sheets_show_simple_dialog),
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            buttonType = ButtonType.Secondary,
            onClick = component::onShowCustomDialogControlClick,
            text = stringResource(R.string.navigation_bottom_sheets_show_custom_dialog),
            modifier = Modifier.fillMaxWidth()
        )
    }

    BottomSheet(component.simpleDialogControl) {
        NameClearDialogUi(
            name = name,
            onClearClick = component::onClearTextClick
        )
    }

    BottomSheet(component.customDialogControl) {
        NavigationCustomDialogUi(it)
    }
}

@Preview
@Composable
private fun NavigationBottomSheetsUiPreview() {
    AppTheme {
        NavigationBottomSheetsUi(FakeNavigationBottomSheetsComponent())
    }
}
