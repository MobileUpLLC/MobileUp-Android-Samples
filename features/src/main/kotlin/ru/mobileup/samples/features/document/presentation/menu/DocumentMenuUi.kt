package ru.mobileup.samples.features.document.presentation.menu

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

private const val DOCUMENT_MIME_TYPE = "application/pdf"

@Composable
fun DocumentMenuUi(
    component: DocumentMenuComponent,
    modifier: Modifier = Modifier
) {
    val pickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            component.onPreviewClick(uri)
        }

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
            text = stringResource(R.string.document_menu_item_preview),
            onClick = {
                pickerLauncher.launch(arrayOf(DOCUMENT_MIME_TYPE))
            }
        )
    }
}

@Preview
@Composable
private fun DocumentMenuUiPreview() {
    AppTheme {
        DocumentMenuUi(FakeDocumentMenuComponent())
    }
}