package ru.mobileup.samples.features.tutorial.presentation.sample.tutorial

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.tutorial.data.TutorialStatusStorage
import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.tutorial.domain.TutorialMessage
import ru.mobileup.samples.features.tutorial.domain.TutorialStatus

class RealTutorialManagementSampleComponent(
    componentContext: ComponentContext,
    override val tutorialManager: TutorialManager,
    private val tutorialStatusStorage: TutorialStatusStorage,
    private val errorHandler: ErrorHandler
) : ComponentContext by componentContext, TutorialManagementSampleComponent {
    private val tutorialMessages = listOf(
        TutorialMessage(
            key = TutorialMessageKeys.All,
            text = StringDesc.Resource(R.string.tutorial_text_all),
            onActionClick = ::proceedWithTutorial
        ),
        TutorialMessage(
            key = TutorialMessageKeys.First,
            text = StringDesc.Resource(R.string.tutorial_text_first),
            onActionClick = ::proceedWithTutorial
        ),
        TutorialMessage(
            key = TutorialMessageKeys.Second,
            text = StringDesc.Resource(R.string.tutorial_text_second),
            onActionClick = ::proceedWithTutorial
        ),
        TutorialMessage(
            key = TutorialMessageKeys.Third,
            text = StringDesc.Resource(R.string.tutorial_text_third),
            onActionClick = ::proceedWithTutorial
        ),
        TutorialMessage(
            key = TutorialMessageKeys.Back,
            text = StringDesc.Resource(R.string.tutorial_text_back),
            onActionClick = ::proceedWithTutorial
        ),
        TutorialMessage(
            key = TutorialMessageKeys.Title,
            text = StringDesc.Resource(R.string.tutorial_text_title),
            onActionClick = ::proceedWithTutorial
        )
    )

    private var currentIndex: Int = 0

    init {
        componentScope.safeLaunch(errorHandler) {
            when (val status = tutorialStatusStorage.getStatus()) {
                TutorialStatus.Finished -> { /* do nothing */ }
                is TutorialStatus.InProgress -> {
                    currentIndex = status.messageIndex
                    tutorialManager.showMessage(tutorialMessages[currentIndex])
                }
                TutorialStatus.NotStarted -> {
                    currentIndex = 0
                    tutorialManager.showMessage(tutorialMessages[currentIndex])
                }
            }
        }

        tutorialManager
            .skipTutorialEventFlow
            .onEach {
                tutorialStatusStorage.setStatus(TutorialStatus.Finished)
                tutorialManager.hideMessage()
            }
            .launchIn(componentScope)

        Log.d("kursor1337", currentIndex.toString())
    }

    private fun proceedWithTutorial() {
        currentIndex++
        componentScope.safeLaunch(errorHandler) {
            if (currentIndex < tutorialMessages.size) {
                tutorialStatusStorage.setStatus(TutorialStatus.InProgress(currentIndex))
                tutorialManager.showMessage(tutorialMessages[currentIndex])
            } else {
                tutorialStatusStorage.setStatus(TutorialStatus.Finished)
                tutorialManager.hideMessage()
            }
        }
    }
}