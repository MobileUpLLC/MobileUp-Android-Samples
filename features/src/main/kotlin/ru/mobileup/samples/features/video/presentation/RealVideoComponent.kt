package ru.mobileup.samples.features.video.presentation

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
import ru.mobileup.samples.features.video.createVideoMenuComponent
import ru.mobileup.samples.features.video.createVideoPlayerComponent
import ru.mobileup.samples.features.video.createVideoRecorderComponent
import ru.mobileup.samples.features.video.data.VideoFileManager
import ru.mobileup.samples.features.video.data.utils.VideoDirectory
import ru.mobileup.samples.features.video.domain.VideoOption
import ru.mobileup.samples.features.video.presentation.menu.VideoMenuComponent
import ru.mobileup.samples.features.video.presentation.recorder.VideoRecorderComponent

class RealVideoComponent(
    componentContext: ComponentContext,
    videoFileManager: VideoFileManager,
    private val componentFactory: ComponentFactory
) : ComponentContext by componentContext, VideoComponent {

    init {
        componentScope.launch {
            videoFileManager.cleanVideoDirectory(VideoDirectory.Recorder)
            videoFileManager.cleanVideoDirectory(VideoDirectory.Render)
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
    ): VideoComponent.Child = when (config) {
        is ChildConfig.Menu -> {
            VideoComponent.Child.Menu(
                componentFactory.createVideoMenuComponent(
                    componentContext,
                    ::onMenuOutput
                )
            )
        }

        is ChildConfig.Recorder -> {
            VideoComponent.Child.Recorder(
                componentFactory.createVideoRecorderComponent(
                    componentContext,
                    ::onRecorderOutput
                )
            )
        }

        is ChildConfig.Player -> {
            VideoComponent.Child.Player(
                componentFactory.createVideoPlayerComponent(
                    uri = config.uri,
                    componentContext
                )
            )
        }
    }

    private fun onMenuOutput(output: VideoMenuComponent.Output) {
        when (output) {
            is VideoMenuComponent.Output.VideoOptionChosen -> navigation.safePush(
                when (output.videoOption) {
                    VideoOption.Recorder -> ChildConfig.Recorder
                    is VideoOption.Player -> ChildConfig.Player(output.videoOption.uri)
                }
            )
        }
    }

    private fun onRecorderOutput(output: VideoRecorderComponent.Output) {
        when (output) {
            is VideoRecorderComponent.Output.PlayerRequested -> navigation.safePush(
                ChildConfig.Player(uri = output.uri)
            )
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Recorder : ChildConfig

        @Serializable
        data class Player(
            @Serializable(with = UriSerializer::class) val uri: Uri
        ) : ChildConfig
    }
}