package ru.mobileup.samples.features.photo

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.photo.data.PhotoFileManager
import ru.mobileup.samples.features.photo.data.PhotoFileManagerImpl
import ru.mobileup.samples.features.photo.presentation.PhotoComponent
import ru.mobileup.samples.features.photo.presentation.RealPhotoComponent
import ru.mobileup.samples.features.photo.presentation.camera.PhotoCameraComponent
import ru.mobileup.samples.features.photo.presentation.camera.RealPhotoCameraComponent
import ru.mobileup.samples.features.photo.presentation.menu.PhotoMenuComponent
import ru.mobileup.samples.features.photo.presentation.menu.RealPhotoMenuComponent
import ru.mobileup.samples.features.photo.presentation.preview.PhotoPreviewComponent
import ru.mobileup.samples.features.photo.presentation.preview.RealPhotoPreviewComponent

val photoModule = module {
    single<PhotoFileManager> { PhotoFileManagerImpl(get()) }
}

fun ComponentFactory.createPhotoComponent(componentContext: ComponentContext): PhotoComponent {
    return RealPhotoComponent(componentContext, get(), get())
}

fun ComponentFactory.createPhotoMenuComponent(
    componentContext: ComponentContext,
    onOutput: (PhotoMenuComponent.Output) -> Unit
): PhotoMenuComponent {
    return RealPhotoMenuComponent(componentContext, get(), onOutput)
}

fun ComponentFactory.createPhotoCameraComponent(
    componentContext: ComponentContext,
    onOutput: (PhotoCameraComponent.Output) -> Unit
): PhotoCameraComponent {
    return RealPhotoCameraComponent(componentContext, onOutput, get())
}

fun ComponentFactory.createPhotoPreviewComponent(
    uris: List<Uri>,
    componentContext: ComponentContext
): PhotoPreviewComponent {
    return RealPhotoPreviewComponent(uris, componentContext, get(), get(), get(), get())
}