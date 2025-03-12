package ru.mobileup.samples.features.root.presentation

import ru.mobileup.samples.core.message.presentation.FakeMessageComponent
import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.features.menu.presentation.FakeMenuComponent
import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.tutorial.domain.TutorialManagerImpl

class FakeRootComponent : RootComponent {

    override val childStack = createFakeChildStackStateFlow(
        RootComponent.Child.Menu(FakeMenuComponent())
    )
    override val tutorialManager: TutorialManager = TutorialManagerImpl()

    override val messageComponent = FakeMessageComponent()
}
