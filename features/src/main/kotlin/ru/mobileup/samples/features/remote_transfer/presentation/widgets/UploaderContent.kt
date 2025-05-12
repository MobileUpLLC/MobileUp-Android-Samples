package ru.mobileup.samples.features.remote_transfer.presentation.widgets

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.remote_transfer.domain.progress.UploadProgress
import ru.mobileup.samples.features.remote_transfer.domain.states.UploaderState
import ru.mobileup.samples.features.remote_transfer.presentation.RemoteTransferComponent

@Composable
fun UploaderContent(
    component: RemoteTransferComponent,
    uploaderState: UploaderState,
    onOpenDownloader: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { component.onFilePicked(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextAccent(
                text = uploaderState.selectedUri?.toString() ?: "...",
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            AppButton(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.remote_transfer_pick_file_btn),
                onClick = {
                    pickerLauncher.launch(arrayOf("*/*"))
                }
            )
        }

        if (uploaderState.selectedUri != null) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.remote_transfer_upload_btn),
                onClick = {
                    component.onUploadFileClick(uploaderState.selectedUri)
                }
            )

            Text(
                text = stringResource(R.string.remote_transfer_upload_caption),
                style = CustomTheme.typography.caption.regular,
                color = CustomTheme.colors.text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uploaderState.uploads.values.toList()) { upload ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CustomTheme.colors.background.tertiary)
                        .padding(all = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.remote_transfer_file_uri) + " " + upload.uri,
                        style = CustomTheme.typography.caption.regular,
                        color = CustomTheme.colors.text.primary,
                    )

                    when (upload) {
                        is UploadProgress.Uploading -> {
                            RemoteProcessingIndicator(
                                bytesProcessed = upload.bytesProcessed,
                                bytesTotal = upload.bytesTotal
                            )
                        }

                        is UploadProgress.Completed -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.remote_transfer_file_link) + " " + upload.link,
                                    color = CustomTheme.colors.text.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_download),
                                    contentDescription = "download",
                                    tint = CustomTheme.colors.icon.primary,
                                    modifier = Modifier.clickable {
                                        component.onDownloadFileClick(upload.uri)
                                        onOpenDownloader()
                                    }
                                )
                            }
                        }

                        is UploadProgress.Failed -> {
                            Text(
                                text = stringResource(R.string.remote_transfer_upload_failed),
                                color = CustomTheme.colors.text.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}