package ru.mobileup.samples.features.uploader.presentation

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mobileup.samples.features.uploader.domain.states.UploaderState

class FakeUploaderComponent : UploaderComponent {
    override val uploaderState = MutableStateFlow(UploaderState.build())
    override fun onPickFileClick(uri: Uri) = Unit
    override fun onUploadFileClick(uri: Uri) = Unit
    override fun onCopyClick(url: String) = Unit
    override fun onDownloadWithKtorClick(url: String) = Unit
    override fun onDownloadWithManagerClick(url: String) = Unit
}