package ru.mobileup.samples.features.uploader.presentation

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
import ru.mobileup.samples.features.uploader.data.ClipboardManager
import ru.mobileup.samples.features.uploader.data.DownloadRepository
import ru.mobileup.samples.features.uploader.data.UploadRepository
import ru.mobileup.samples.features.uploader.domain.progress.DownloadProgress
import ru.mobileup.samples.features.uploader.domain.progress.UploadProgress
import ru.mobileup.samples.features.uploader.domain.states.UploaderState

class RealUploaderComponent(
    componentContext: ComponentContext,
    private val uploadRepository: UploadRepository,
    private val downloadRepository: DownloadRepository,
    private val permissionService: PermissionService,
    private val clipboardManager: ClipboardManager,
    private val messageService: MessageService,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, UploaderComponent {

    override val uploaderState = MutableStateFlow(UploaderState())

    init {
        componentScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionService.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onFilePicked(uri: Uri) {
        uploaderState.update {
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
                Message(text = StringDesc.Resource(R.string.uploader_link_copied))
            )
        }
    }

    override fun onUploadFileClick(uri: Uri) {
        uploadRepository.upload(uri).onEach { uploadProgress ->
            uploaderState.update {
                it.copy(uploadProgress = uploadProgress)
            }

            when (uploadProgress) {
                is UploadProgress.Completed -> {
                    messageService.showMessage(
                        Message(text = StringDesc.Resource(R.string.uploader_upload_completed))
                    )
                }

                UploadProgress.Failed -> {
                    messageService.showMessage(
                        Message(text = StringDesc.Resource(R.string.uploader_upload_failed))
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
                Message(text = StringDesc.Resource(R.string.uploader_download_start_manager))
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
        uploaderState.update {
            it.copy(downloadProgress = downloadProgress)
        }

        when (downloadProgress) {
            DownloadProgress.Completed -> {
                messageService.showMessage(
                    Message(text = StringDesc.Resource(R.string.uploader_download_completed))
                )
            }

            DownloadProgress.Failed -> {
                messageService.showMessage(
                    Message(text = StringDesc.Resource(R.string.uploader_download_failed))
                )
            }

            else -> {
                // Do nothing
            }
        }
    }
}