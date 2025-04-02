package ru.mobileup.samples.features.photo.presentation

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.UriSerializer
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.photo.createPhotoCameraComponent
import ru.mobileup.samples.features.photo.createPhotoMenuComponent
import ru.mobileup.samples.features.photo.createPhotoPreviewComponent
import ru.mobileup.samples.features.photo.data.PhotoFileManager
import ru.mobileup.samples.features.photo.data.utils.PhotoDirectory
import ru.mobileup.samples.features.photo.presentation.camera.PhotoCameraComponent
import ru.mobileup.samples.features.photo.presentation.menu.PhotoMenuComponent

class RealPhotoComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    photoFileManager: PhotoFileManager
) : ComponentContext by componentContext, PhotoComponent {

    init {
        componentScope.launch {
            photoFileManager.cleanPhotoDirectory(PhotoDirectory.Camera)
        }
    }

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): PhotoComponent.Child = when (config) {
        is ChildConfig.Menu -> {
            PhotoComponent.Child.Menu(
                componentFactory.createPhotoMenuComponent(
                    componentContext,
                    ::onMenuOutput
                )
            )
        }

        is ChildConfig.Camera -> {
            PhotoComponent.Child.Camera(
                componentFactory.createPhotoCameraComponent(
                    componentContext,
                    ::onCameraOutput
                )
            )
        }

        is ChildConfig.Preview -> {
            PhotoComponent.Child.Preview(
                componentFactory.createPhotoPreviewComponent(
                    config.uris,
                    componentContext
                )
            )
        }
    }

    private fun onMenuOutput(output: PhotoMenuComponent.Output) {
        when (output) {
            is PhotoMenuComponent.Output.CameraRequested -> navigation.safePush(ChildConfig.Camera)
            is PhotoMenuComponent.Output.PreviewRequested -> navigation.safePush(
                ChildConfig.Preview(output.uris)
            )
        }
    }

    private fun onCameraOutput(output: PhotoCameraComponent.Output) {
        when (output) {
            is PhotoCameraComponent.Output.PreviewRequested -> navigation.safePush(
                ChildConfig.Preview(output.uris)
            )
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Camera : ChildConfig

        @Serializable
        data class Preview(
            val uris: List<@Serializable(with = UriSerializer::class) Uri>
        ) : ChildConfig
    }
}