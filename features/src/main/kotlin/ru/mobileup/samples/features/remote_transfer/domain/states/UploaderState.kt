package ru.mobileup.samples.features.remote_transfer.domain.states

import android.net.Uri
import ru.mobileup.samples.features.remote_transfer.domain.progress.UploadProgress

data class UploaderState(
    val selectedUri: Uri? = null,
    val uploads: Map<Uri, UploadProgress> = emptyMap()
)