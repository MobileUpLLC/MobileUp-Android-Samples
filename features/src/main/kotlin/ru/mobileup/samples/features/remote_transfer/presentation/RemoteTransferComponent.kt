package ru.mobileup.samples.features.remote_transfer.presentation

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.kmm_form_validation.control.InputControl
import ru.mobileup.samples.features.remote_transfer.domain.RemoteTransferTab
import ru.mobileup.samples.features.remote_transfer.domain.states.RemoteTransferState

interface RemoteTransferComponent {

    val selectedTab: StateFlow<RemoteTransferTab>

    val remoteTransferState: StateFlow<RemoteTransferState>

    val linkInputControl: InputControl

    fun onTabSelect(tab: RemoteTransferTab)

    fun onFilePicked(uri: Uri)

    fun onUploadFileClick(uri: Uri)

    fun onDownloadFileClick(uri: Uri)

    fun onCopyClick(url: String)

    fun onDownloadWithKtorClick(url: String)

    fun onDownloadWithDownloadManagerClick(url: String)

    fun onDownloadWithWorkManagerClick(url: String)
}