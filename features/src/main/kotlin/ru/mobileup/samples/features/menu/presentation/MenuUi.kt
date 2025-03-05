package ru.mobileup.samples.features.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.menu.domain.Sample

@Composable
fun MenuUi(
    component: MenuComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_form),
            onClick = {
                component.onButtonClick(Sample.Form)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_video),
            onClick = {
                component.onButtonClick(Sample.Video)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_calendar),
            onClick = {
                component.onButtonClick(Sample.Calendar)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_qr_code_generator),
            onClick = {
                component.onButtonClick(Sample.QrCodeGenerator)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_qr_code_scanner),
            onClick = {
                component.onButtonClick(Sample.QrCodeScanner)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_chart),
            onClick = {
                component.onButtonClick(Sample.Chart)
            }
        )

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.menu_item_navigation),
            onClick = {
                component.onButtonClick(Sample.Navigation)
            }
        )
    }
}

@Preview
@Composable
private fun MenuUiPreview() {
    AppTheme {
        MenuUi(FakeMenuComponent())
    }
}