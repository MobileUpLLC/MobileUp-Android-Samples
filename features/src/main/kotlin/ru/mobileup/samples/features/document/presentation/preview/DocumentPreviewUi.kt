package ru.mobileup.samples.features.document.presentation.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.widget.FullscreenCircularProgress
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.document.domain.DocumentMetadata

@Composable
fun DocumentPreviewUi(
    component: DocumentPreviewComponent,
    modifier: Modifier = Modifier
) {
    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
        navigationBarIconsColor = SystemBarIconsColor.Light
    )

    DocumentPreviewContent(component, modifier)
}

@Composable
private fun DocumentPreviewContent(
    component: DocumentPreviewComponent,
    modifier: Modifier = Modifier
) {
    val documentMetadataState by component.documentMetadataState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            PreviewTopBar()
        }
    ) { paddingValues ->
        documentMetadataState?.let { documentMetadata ->
            DocumentPreview(
                documentMetadata = documentMetadata,
                onShareMetadataClick = component::onShareMetadataClick,
                onShareClick = component::onShareClick,
                onOpenClick = component::onOpenClick,
                modifier = Modifier.padding(paddingValues)
            )
        } ?: run {
            FullscreenCircularProgress(
                modifier = Modifier.padding()
            )
        }
    }
}

@Composable
private fun PreviewTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.palette.black)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.document_menu_item_preview),
            color = CustomTheme.colors.palette.white,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun DocumentPreview(
    documentMetadata: DocumentMetadata,
    onShareMetadataClick: () -> Unit,
    onShareClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        MetadataField(
            title = stringResource(R.string.document_field_name),
            value = documentMetadata.name
        )

        MetadataField(
            title = stringResource(R.string.document_field_size),
            value = documentMetadata.size
        )

        MetadataField(
            title = stringResource(R.string.document_field_date),
            value = documentMetadata.dateModified
        )

        MetadataField(
            title = stringResource(R.string.document_field_mime),
            value = documentMetadata.mime
        )

        MetadataField(
            title = stringResource(R.string.document_field_flags),
            value = documentMetadata.flags
        )

        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            buttonType = ButtonType.Secondary,
            text = stringResource(R.string.document_metadata_share),
            onClick = onShareMetadataClick,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            AppButton(
                modifier = Modifier.weight(1f),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.document_open),
                onClick = onOpenClick,
            )

            IconButton(
                onClick = onShareClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "share"
                )
            }
        }
    }
}

@Composable
private fun MetadataField(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = CustomTheme.typography.body.regular,
            color = CustomTheme.colors.text.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Bottom)
        )
    }

    HorizontalDivider(
        color = CustomTheme.colors.palette.black10, thickness = 1.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview
@Composable
private fun DocumentPreviewUiPreview() {
    AppTheme {
        DocumentPreviewUi(FakeDocumentPreviewComponent())
    }
}