package ru.mobileup.samples.features.tutorial.presentation.sample.tutorial

import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.tutorial.domain.TutorialManagerImpl

class FakeTutorialManagementSampleComponent : TutorialManagementSampleComponent {
    override val tutorialManager: TutorialManager = TutorialManagerImpl()
}