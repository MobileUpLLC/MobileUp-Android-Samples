package ru.mobileup.samples.features.uploader.presentation

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.uploader.domain.states.UploaderState

interface UploaderComponent {

    val uploaderState: StateFlow<UploaderState>

    fun onPickFileClick(uri: Uri)

    fun onUploadFileClick(uri: Uri)

    fun onCopyClick(url: String)

    fun onDownloadWithKtorClick(url: String)

    fun onDownloadWithManagerClick(url: String)
}