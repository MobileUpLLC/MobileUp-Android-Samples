package ru.mobileup.samples.features.photo.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.photo.presentation.camera.PhotoCameraComponent
import ru.mobileup.samples.features.photo.presentation.cropping.PhotoCroppingComponent
import ru.mobileup.samples.features.photo.presentation.menu.PhotoMenuComponent
import ru.mobileup.samples.features.photo.presentation.preview.PhotoPreviewComponent

interface PhotoComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Menu(val component: PhotoMenuComponent) : Child
        class Camera(val component: PhotoCameraComponent) : Child
        class Preview(val component: PhotoPreviewComponent) : Child
        class Cropping(val component: PhotoCroppingComponent) : Child
    }
}