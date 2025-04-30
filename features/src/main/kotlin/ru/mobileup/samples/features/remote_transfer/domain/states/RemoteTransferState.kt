package ru.mobileup.samples.features.remote_transfer.domain.states

import android.net.Uri
import ru.mobileup.samples.features.remote_transfer.domain.progress.DownloadProgress
import ru.mobileup.samples.features.remote_transfer.domain.progress.UploadProgress

data class RemoteTransferState(
    val uri: Uri? = null,
    val uploadProgress: UploadProgress? = null,
    val downloadProgress: DownloadProgress? = null
)