package ru.mobileup.samples.features.uploader

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.uploader.data.ClipboardManager
import ru.mobileup.samples.features.uploader.data.ClipboardManagerImpl
import ru.mobileup.samples.features.uploader.data.DownloadRepository
import ru.mobileup.samples.features.uploader.data.DownloadRepositoryImpl
import ru.mobileup.samples.features.uploader.data.UploadRepository
import ru.mobileup.samples.features.uploader.data.UploadRepositoryImpl
import ru.mobileup.samples.features.uploader.presentation.RealUploaderComponent
import ru.mobileup.samples.features.uploader.presentation.UploaderComponent

val uploaderModule = module {
    single<UploadRepository> { UploadRepositoryImpl(get()) }
    single<DownloadRepository> { DownloadRepositoryImpl(get()) }
    single<ClipboardManager> { ClipboardManagerImpl(get()) }
}

fun ComponentFactory.createUploaderComponent(componentContext: ComponentContext): UploaderComponent {
    return RealUploaderComponent(componentContext, get(), get(), get(), get(), get())
}