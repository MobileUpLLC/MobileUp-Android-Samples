package ru.mobileup.samples.features.remote_transfer.presentation.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.widget.button.AppButton
import ru.mobileup.samples.core.widget.button.ButtonType
import ru.mobileup.samples.core.widget.text_field.AppTextField
import ru.mobileup.samples.core.widget.text_field.AppTextFieldDefaults
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.remote_transfer.domain.progress.DownloadProgress
import ru.mobileup.samples.features.remote_transfer.domain.states.DownloaderState
import ru.mobileup.samples.features.remote_transfer.presentation.RemoteTransferComponent

@Composable
fun DownloaderContent(
    component: RemoteTransferComponent,
    downloaderState: DownloaderState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AppTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = stringResource(R.string.remote_transfer_link_label),
            inputControl = component.linkInputControl,
            placeholder = "...",
            colors = AppTextFieldDefaults.colors.copy(
                focusedContainerColor = CustomTheme.colors.background.tertiary,
                unfocusedContainerColor = CustomTheme.colors.background.tertiary,
                errorContainerColor = CustomTheme.colors.background.tertiary,
                unfocusedTextColor = CustomTheme.colors.text.primary,
                focusedTextColor = CustomTheme.colors.text.primary
            ),
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            component.onCopyClick(downloaderState.selectedLink)
                        },
                    painter = painterResource(
                        R.drawable.ic_share
                    ),
                    contentDescription = null
                )
            },
            shape = RectangleShape,
        )

        AnimatedVisibility(
            visible = downloaderState.isValid,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            fun onResetFocus() {
                component.linkInputControl.onFocusChange(false)
                keyboardController?.hide()
            }

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    buttonType = ButtonType.Secondary,
                    text = stringResource(R.string.remote_transfer_download_with_ktor_btn),
                    onClick = {
                        onResetFocus()
                        component.onDownloadWithKtorClick(downloaderState.selectedLink)
                    }
                )

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    buttonType = ButtonType.Secondary,
                    text = stringResource(R.string.remote_transfer_download_with_download_manager_btn),
                    onClick = {
                        onResetFocus()
                        component.onDownloadWithDownloadManagerClick(downloaderState.selectedLink)
                    }
                )

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    buttonType = ButtonType.Secondary,
                    text = stringResource(R.string.remote_transfer_download_with_work_manager_btn),
                    onClick = {
                        onResetFocus()
                        component.onDownloadWithWorkManagerClick(downloaderState.selectedLink)
                    }
                )

                when (val downloadProgress = downloaderState.downloadProgress) {
                    is DownloadProgress.InProgress -> {
                        RemoteProcessingIndicator(
                            bytesProcessed = downloadProgress.bytesProcessed,
                            bytesTotal = downloadProgress.bytesTotal
                        )
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }
}