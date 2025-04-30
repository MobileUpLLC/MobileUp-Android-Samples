package ru.mobileup.samples.features.remote_transfer.presentation

import android.Manifest
import android.net.Uri
import android.os.Build
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeRun
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.remote_transfer.data.ClipboardManager
import ru.mobileup.samples.features.remote_transfer.data.DownloadRepository
import ru.mobileup.samples.features.remote_transfer.data.UploadRepository
import ru.mobileup.samples.features.remote_transfer.domain.progress.DownloadProgress
import ru.mobileup.samples.features.remote_transfer.domain.progress.UploadProgress
import ru.mobileup.samples.features.remote_transfer.domain.states.RemoteTransferState

class RealRemoteTransferComponent(
    componentContext: ComponentContext,
    private val uploadRepository: UploadRepository,
    private val downloadRepository: DownloadRepository,
    private val permissionService: PermissionService,
    private val clipboardManager: ClipboardManager,
    private val messageService: MessageService,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, RemoteTransferComponent {

    override val remoteTransferState = MutableStateFlow(RemoteTransferState())

    init {
        componentScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionService.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onFilePicked(uri: Uri) {
        remoteTransferState.update {
            it.copy(
                uri = uri,
                uploadProgress = null
            )
        }
    }

    override fun onCopyClick(url: String) {
        clipboardManager.copyToClipboard(url)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            messageService.showMessage(
                Message(text = StringDesc.Resource(R.string.remote_transfer_link_copied))
            )
        }
    }

    override fun onUploadFileClick(uri: Uri) {
        uploadRepository.upload(uri).onEach { uploadProgress ->
            remoteTransferState.update {
                it.copy(uploadProgress = uploadProgress)
            }

            when (uploadProgress) {
                is UploadProgress.Completed -> {
                    messageService.showMessage(
                        Message(text = StringDesc.Resource(R.string.remote_transfer_upload_completed))
                    )
                }

                UploadProgress.Failed -> {
                    messageService.showMessage(
                        Message(text = StringDesc.Resource(R.string.remote_transfer_upload_failed))
                    )
                }

                else -> {
                    // Do nothing
                }
            }
        }.launchIn(componentScope)
    }

    override fun onDownloadWithKtorClick(url: String) {
        downloadRepository.downloadWithKtor(url).onEach { downloadProgress ->
            processDownloadProgress(downloadProgress)
        }.launchIn(componentScope)
    }

    override fun onDownloadWithDownloadManagerClick(url: String) {
        safeRun(errorHandler) {
            downloadRepository.downloadWithDownloadManager(url)
            messageService.showMessage(
                Message(text = StringDesc.Resource(R.string.remote_transfer_download_start_manager))
            )
        }
    }

    override fun onDownloadWithWorkManagerClick(url: String) {
        downloadRepository.downloadWithWorkManager(url).onEach { downloadProgress ->
            processDownloadProgress(downloadProgress)
        }.launchIn(componentScope)
    }

    private fun processDownloadProgress(
        downloadProgress: DownloadProgress
    ) {
        remoteTransferState.update {
            it.copy(downloadProgress = downloadProgress)
        }

        when (downloadProgress) {
            DownloadProgress.Completed -> {
                messageService.showMessage(
                    Message(text = StringDesc.Resource(R.string.remote_transfer_download_completed))
                )
            }

            DownloadProgress.Failed -> {
                messageService.showMessage(
                    Message(text = StringDesc.Resource(R.string.remote_transfer_download_failed))
                )
            }

            else -> {
                // Do nothing
            }
        }
    }
}