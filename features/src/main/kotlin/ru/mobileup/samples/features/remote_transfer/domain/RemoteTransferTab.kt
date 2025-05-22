package ru.mobileup.samples.features.remote_transfer.domain

import dev.icerock.moko.resources.desc.StringDesc
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.features.R

enum class RemoteTransferTab {
    Upload, Download
}

fun RemoteTransferTab.toStringRes() = when (this) {
    RemoteTransferTab.Upload -> StringDesc.Resource(R.string.remote_transfer_tag_upload)
    RemoteTransferTab.Download -> StringDesc.Resource(R.string.remote_transfer_tag_download)
}