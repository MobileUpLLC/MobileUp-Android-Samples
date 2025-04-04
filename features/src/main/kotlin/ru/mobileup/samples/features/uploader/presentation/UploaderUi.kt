package ru.mobileup.samples.features.uploader.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.uploader.domain.progress.DownloadProgress
import ru.mobileup.samples.features.uploader.domain.progress.UploadProgress

@Composable
fun UploaderUi(
    component: UploaderComponent,
    modifier: Modifier = Modifier
) {
    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
        navigationBarIconsColor = SystemBarIconsColor.Light
    )

    UploaderContent(
        component = component,
        modifier = modifier
    )
}

@Composable
private fun UploaderContent(
    component: UploaderComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            UploaderTopBar()
        }
    ) { paddingValues ->
        Uploader(
            component = component,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun UploaderTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.palette.black)
            .padding(top = 16.dp)
            .padding(horizontal = 8.dp, vertical = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.uploader_title),
            color = CustomTheme.colors.text.invert,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Uploader(
    component: UploaderComponent,
    modifier: Modifier = Modifier
) {
    val uploaderState by component.uploaderState.collectAsState()

    val pickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { component.onPickFileClick(it) }
    }

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextAccent(
                text = uploaderState.uri?.toString() ?: "...",
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            AppButton(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.uploader_pick_file_btn),
                onClick = {
                    pickerLauncher.launch(arrayOf("*/*"))
                }
            )
        }

        if (uploaderState.uri != null) {
            AppButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                buttonType = ButtonType.Secondary,
                text = stringResource(R.string.uploader_upload_btn),
                onClick = {
                    uploaderState.uri?.let {
                        component.onUploadFileClick(it)
                    }
                }
            )

            Text(
                text = stringResource(R.string.uploader_upload_caption),
                style = CustomTheme.typography.caption.regular,
                color = CustomTheme.colors.text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            when (val uploadProgress = uploaderState.uploadProgress) {
                is UploadProgress.Uploading -> {
                    ProgressIndicator(
                        bytesProcessed = uploadProgress.bytesProcessed,
                        bytesTotal = uploadProgress.bytesTotal
                    )
                }

                is UploadProgress.Completed -> {
                    Text(
                        text = stringResource(R.string.uploader_file_link),
                        color = CustomTheme.colors.text.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextAccent(
                            text = uploadProgress.link,
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                        )

                        AppButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(start = 8.dp),
                            buttonType = ButtonType.Secondary,
                            text = stringResource(R.string.uploader_copy_btn),
                            onClick = {
                                component.onCopyClick(uploadProgress.link)
                            }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    ) {
                        AppButton(
                            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                            buttonType = ButtonType.Secondary,
                            text = stringResource(R.string.uploader_download_ktor_btn),
                            onClick = {
                                component.onDownloadWithKtorClick(uploadProgress.link)
                            }
                        )

                        AppButton(
                            modifier = Modifier.weight(1f),
                            buttonType = ButtonType.Secondary,
                            text = stringResource(R.string.uploader_download_manager_btn),
                            onClick = {
                                component.onDownloadWithManagerClick(uploadProgress.link)
                            }
                        )
                    }

                    when (val downloadProgress = uploaderState.downloadProgress) {
                        is DownloadProgress.InProgress -> {
                            ProgressIndicator(
                                bytesProcessed = downloadProgress.bytesProcessed,
                                bytesTotal = downloadProgress.bytesTotal
                            )
                        }

                        else -> {
                            // Do nothing
                        }
                    }
                }

                null -> {
                    // Do nothing
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    bytesProcessed: Long,
    bytesTotal: Long
) {
    val progressString by remember(bytesProcessed, bytesTotal) {
        derivedStateOf {
            formatFileSize(bytesProcessed) + " / " + formatFileSize(bytesTotal)
        }
    }

    LinearProgressIndicator(
        progress = { bytesProcessed.toFloat() / bytesTotal },
        modifier = Modifier.fillMaxWidth()
    )

    Text(
        text = progressString,
        color = CustomTheme.colors.text.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
private fun TextAccent(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CustomTheme.colors.palette.black10)
            .padding(8.dp)
    )
}

private fun formatFileSize(sizeBytes: Long): String {
    return when {
        sizeBytes >= 1024 * 1024 -> "%.1fmB".format(sizeBytes / (1024.0 * 1024.0))
        sizeBytes >= 1024 -> "${(sizeBytes / 1024).toInt()}kB"
        else -> "${sizeBytes}B"
    }
}

@Preview
@Composable
private fun UploaderUiPreview() {
    AppTheme {
        UploaderUi(FakeUploaderComponent())
    }
}