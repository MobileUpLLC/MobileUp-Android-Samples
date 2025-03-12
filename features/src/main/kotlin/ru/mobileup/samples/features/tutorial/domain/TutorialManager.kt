package ru.mobileup.samples.features.tutorial.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TutorialManager {

    val visibleMessage: StateFlow<TutorialMessage?>
    val skipTutorialEventFlow: Flow<Unit>

    fun showMessage(message: TutorialMessage)

    fun hideMessage()

    fun skipTutorial()
}