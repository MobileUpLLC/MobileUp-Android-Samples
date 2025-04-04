package ru.mobileup.samples.features.root.presentation

import ru.mobileup.samples.core.message.presentation.FakeMessageComponent
import ru.mobileup.samples.core.theme.component.FakeThemeComponent
import ru.mobileup.samples.core.tutorial.presentation.overlay.FakeTutorialOverlayComponent
import ru.mobileup.samples.core.tutorial.presentation.overlay.TutorialOverlayComponent
import ru.mobileup.samples.core.utils.createFakeChildStackStateFlow
import ru.mobileup.samples.features.menu.presentation.FakeMenuComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.CheckPinCodeManagementComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.FakeCheckPinCodeManagementComponent

class FakeRootComponent : RootComponent {

    override val childStack = createFakeChildStackStateFlow(
        RootComponent.Child.Menu(FakeMenuComponent())
    )

    override val tutorialOverlayComponent: TutorialOverlayComponent = FakeTutorialOverlayComponent()

    override val messageComponent = FakeMessageComponent()

    override val checkPinCodeManagementComponent: CheckPinCodeManagementComponent =
        FakeCheckPinCodeManagementComponent()

    override val themeComponent = FakeThemeComponent()
}
