package ru.mobileup.samples.features.remote_transfer.presentation

import android.Manifest
import android.net.Uri
import android.os.Build
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mobileup.kmm_form_validation.control.CheckControl
import ru.mobileup.kmm_form_validation.options.ImeAction
import ru.mobileup.kmm_form_validation.options.KeyboardOptions
import ru.mobileup.kmm_form_validation.options.KeyboardType
import ru.mobileup.kmm_form_validation.validation.control.isNotBlank
import ru.mobileup.kmm_form_validation.validation.control.validation
import ru.mobileup.kmm_form_validation.validation.form.FormValidator
import ru.mobileup.kmm_form_validation.validation.form.RevalidateOnValueChanged
import ru.mobileup.kmm_form_validation.validation.form.SetFocusOnFirstInvalidControlAfterValidation
import ru.mobileup.kmm_form_validation.validation.form.ValidateOnFocusLost
import ru.mobileup.kmm_form_validation.validation.form.checked
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeRun
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.utils.CheckControl
import ru.mobileup.samples.core.utils.InputControl
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.formValidator
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.remote_transfer.data.ClipboardManager
import ru.mobileup.samples.features.remote_transfer.data.DownloadRepository
import ru.mobileup.samples.features.remote_transfer.data.UploadRepository
import ru.mobileup.samples.features.remote_transfer.domain.RemoteTransferTab
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

    private val linkCheckControl: CheckControl = CheckControl()

    override val selectedTab = MutableStateFlow(RemoteTransferTab.Upload)

    override val remoteTransferState = MutableStateFlow(RemoteTransferState())

    override val linkInputControl = InputControl(
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
    )

    private val formValidator: FormValidator = formValidator {
        features = listOf(
            ValidateOnFocusLost,
            RevalidateOnValueChanged,
            SetFocusOnFirstInvalidControlAfterValidation
        )

        input(linkInputControl) {
            isNotBlank(R.string.remote_transfer_link_empty.strResDesc())

            validation(
                isValid = ::isLinkValid,
                errorMessage = StringDesc.Resource(R.string.remote_transfer_link_invalid)
            )
        }

        checked(
            linkCheckControl,
            ru.mobileup.samples.core.R.string.checkbox_error_terms.strResDesc()
        )
    }

    init {
        componentScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionService.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        componentScope.launch {
            linkInputControl.value.collect { value ->
                formValidator.validate()

                remoteTransferState.update {
                    it.copy(
                        downloaderState = it.downloaderState.copy(
                            selectedLink = value,
                            isValid = isLinkValid(value)
                        )
                    )
                }
            }
        }
    }

    override fun onTabSelect(tab: RemoteTransferTab) {
        selectedTab.update {
            tab
        }
    }

    override fun onFilePicked(uri: Uri) {
        remoteTransferState.update {
            it.copy(
                uploaderState = it.uploaderState.copy(
                    selectedUri = uri
                )
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
        if (remoteTransferState.value.uploaderState.uploads.containsKey(uri)) {
            messageService.showMessage(
                Message(text = StringDesc.Resource(R.string.remote_transfer_upload_duplicate))
            )
            return
        }

        uploadRepository.upload(uri).onEach { uploadProgress ->
            remoteTransferState.update {
                it.copy(
                    uploaderState = it.uploaderState.copy(
                        uploads = it.uploaderState.uploads.toMutableMap().apply {
                            set(uri, uploadProgress)
                        }
                    )
                )
            }

            when (uploadProgress) {
                is UploadProgress.Completed -> {
                    messageService.showMessage(
                        Message(text = StringDesc.Resource(R.string.remote_transfer_upload_completed))
                    )
                }

                is UploadProgress.Failed -> {
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

    override fun onDownloadFileClick(uri: Uri) {
        val upload =
            (remoteTransferState.value.uploaderState.uploads[uri] as? UploadProgress.Completed)
                ?: return

        linkInputControl.onValueChange(upload.link)
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
            it.copy(
                downloaderState = it.downloaderState.copy(
                    downloadProgress = downloadProgress
                )
            )
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

    private fun isLinkValid(link: String): Boolean {
        return link.startsWith("https://") || link.startsWith("http://")
    }
}