package ru.mobileup.samples.features.uploader.presentation

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeRun
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
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
    private val clipboardManager: ClipboardManager,
    private val messageService: MessageService,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, UploaderComponent {

    override val uploaderState = MutableStateFlow(UploaderState.build())

    override fun onPickFileClick(uri: Uri) {
        uploaderState.update {
            it.copy(
                uri = uri,
                uploadProgress = null
            )
        }
    }

    override fun onCopyClick(url: String) {
        clipboardManager.copyToClipboard(url)
    }

    override fun onUploadFileClick(uri: Uri) {
        uploadRepository.upload(uri).onEach { uploadProgress ->
            uploaderState.update {
                it.copy(uploadProgress = uploadProgress)
            }

            if (uploadProgress is UploadProgress.Completed) {
                messageService.showMessage(
                    Message(
                        text = StringDesc.Resource(
                            R.string.uploader_upload_completed
                        )
                    )
                )
            }
        }.catch {
            messageService.showMessage(
                Message(
                    text = StringDesc.Resource(
                        R.string.uploader_upload_failed
                    )
                )
            )
        }.launchIn(componentScope)
    }

    override fun onDownloadWithKtorClick(url: String) {
        downloadRepository.downloadWithKtor(url).onEach { downloadProgress ->
            uploaderState.update {
                it.copy(downloadProgress = downloadProgress)
            }

            if (downloadProgress is DownloadProgress.Completed) {
                messageService.showMessage(
                    Message(
                        text = StringDesc.Resource(
                            R.string.uploader_download_completed
                        )
                    )
                )
            }
        }.catch {
            uploaderState.update {
                it.copy(downloadProgress = null)
            }

            messageService.showMessage(
                Message(text = StringDesc.Resource(R.string.uploader_download_failed))
            )
        }.launchIn(componentScope)
    }

    override fun onDownloadWithManagerClick(url: String) {
        safeRun(errorHandler) {
            downloadRepository.downloadWithDownloadManager(url)
        }

        messageService.showMessage(
            Message(text = StringDesc.Resource(R.string.uploader_download_start_manager))
        )
    }
}