package ru.mobileup.samples.features.remote_transfer

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.remote_transfer.data.ClipboardManager
import ru.mobileup.samples.features.remote_transfer.data.ClipboardManagerImpl
import ru.mobileup.samples.features.remote_transfer.data.DownloadRepository
import ru.mobileup.samples.features.remote_transfer.data.DownloadRepositoryImpl
import ru.mobileup.samples.features.remote_transfer.data.UploadRepository
import ru.mobileup.samples.features.remote_transfer.data.UploadRepositoryImpl
import ru.mobileup.samples.features.remote_transfer.presentation.RealRemoteTransferComponent
import ru.mobileup.samples.features.remote_transfer.presentation.RemoteTransferComponent

val remoteTransferModule = module {
    single<UploadRepository> { UploadRepositoryImpl(get()) }
    single<DownloadRepository> { DownloadRepositoryImpl(get()) }
    single<ClipboardManager> { ClipboardManagerImpl(get()) }
}

fun ComponentFactory.createRemoteTransferComponent(componentContext: ComponentContext): RemoteTransferComponent {
    return RealRemoteTransferComponent(componentContext, get(), get(), get(), get(), get(), get())
}