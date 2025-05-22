package ru.mobileup.samples.features.remote_transfer.domain.states

data class RemoteTransferState(
    val uploaderState: UploaderState = UploaderState(),
    val downloaderState: DownloaderState = DownloaderState()
)