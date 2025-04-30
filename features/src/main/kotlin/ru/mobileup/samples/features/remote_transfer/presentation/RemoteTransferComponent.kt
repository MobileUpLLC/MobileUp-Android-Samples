package ru.mobileup.samples.features.remote_transfer.presentation

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.remote_transfer.domain.states.RemoteTransferState

interface RemoteTransferComponent {

    val remoteTransferState: StateFlow<RemoteTransferState>

    fun onFilePicked(uri: Uri)

    fun onUploadFileClick(uri: Uri)

    fun onCopyClick(url: String)

    fun onDownloadWithKtorClick(url: String)

    fun onDownloadWithDownloadManagerClick(url: String)

    fun onDownloadWithWorkManagerClick(url: String)
}