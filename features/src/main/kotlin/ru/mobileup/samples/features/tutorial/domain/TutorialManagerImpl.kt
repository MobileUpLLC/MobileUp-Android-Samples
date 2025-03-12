package ru.mobileup.samples.features.tutorial.domain

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class TutorialManagerImpl : TutorialManager {

    override val visibleMessage: MutableStateFlow<TutorialMessage?> =
        MutableStateFlow(null)

    private val skipTutorialEventChannel = Channel<Unit>()

    override val skipTutorialEventFlow: Flow<Unit> = skipTutorialEventChannel.receiveAsFlow()

    override fun showMessage(message: TutorialMessage) {
        visibleMessage.value = message
    }

    override fun hideMessage() {
        visibleMessage.value = null
    }

    override fun skipTutorial() {
        skipTutorialEventChannel.trySend(Unit)
    }
}