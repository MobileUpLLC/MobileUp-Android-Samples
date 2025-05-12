package ru.mobileup.samples.features.remote_transfer.presentation

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.core.utils.fakeInputControl
import ru.mobileup.samples.features.remote_transfer.domain.RemoteTransferTab
import ru.mobileup.samples.features.remote_transfer.domain.states.RemoteTransferState

class FakeRemoteTransferComponent : RemoteTransferComponent {
    override val selectedTab = MutableStateFlow(RemoteTransferTab.Upload)
    override val remoteTransferState = MutableStateFlow(RemoteTransferState())
    override val linkInputControl = fakeInputControl()
    override fun onTabSelect(tab: RemoteTransferTab) = Unit
    override fun onFilePicked(uri: Uri) = Unit
    override fun onUploadFileClick(uri: Uri) = Unit
    override fun onDownloadFileClick(uri: Uri) = Unit
    override fun onCopyClick(url: String) = Unit
    override fun onDownloadWithKtorClick(url: String) = Unit
    override fun onDownloadWithDownloadManagerClick(url: String) = Unit
    override fun onDownloadWithWorkManagerClick(url: String) = Unit
}