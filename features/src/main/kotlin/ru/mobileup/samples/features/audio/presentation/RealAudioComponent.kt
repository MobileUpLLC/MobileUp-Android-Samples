package ru.mobileup.samples.features.audio.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import kotlinx.serialization.Serializable
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.safePush
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.audio.createAudioMenuComponent
import ru.mobileup.samples.features.audio.createAudioRecordedComponent
import ru.mobileup.samples.features.audio.domain.AudioMenu
import ru.mobileup.samples.features.audio.presentation.menu.AudioMenuComponent

class RealAudioComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, AudioComponent {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Menu,
        serializer = ChildConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    override fun onBackClick() {
        navigation.pop()
    }

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext,
    ): AudioComponent.Child = when (config) {
        ChildConfig.Menu -> AudioComponent.Child.Menu(
            componentFactory.createAudioMenuComponent(
                componentContext,
                ::onAudioMenuOutput
            )
        )
        ChildConfig.Record -> AudioComponent.Child.Recorder(
            componentFactory.createAudioRecordedComponent(
                componentContext
            )
        )
    }

    private fun onAudioMenuOutput(output: AudioMenuComponent.Output) {
        when (output) {
            is AudioMenuComponent.Output.OnMenuChoice -> {
                when (output.audioMenu) {
                    AudioMenu.Recorder -> navigation.safePush(ChildConfig.Record)
                }
            }
        }
    }

    @Serializable
    sealed interface ChildConfig {

        @Serializable
        data object Menu : ChildConfig

        @Serializable
        data object Record : ChildConfig
    }
}