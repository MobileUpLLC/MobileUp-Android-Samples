package ru.mobileup.samples.features.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.message.presentation.MessageComponent
import ru.mobileup.samples.core.theme.component.ThemeComponent
import ru.mobileup.samples.core.tutorial.presentation.overlay.TutorialOverlayComponent
import ru.mobileup.samples.core.utils.PredictiveBackComponent
import ru.mobileup.samples.features.calendar.presentation.CalendarComponent
import ru.mobileup.samples.features.charts.presentation.ChartComponent
import ru.mobileup.samples.features.chat.presentation.ChatComponent
import ru.mobileup.samples.features.collapsing_toolbar.presentation.CollapsingToolbarComponent
import ru.mobileup.samples.features.divkit.presentation.DivKitComponent
import ru.mobileup.samples.features.document.presentation.DocumentComponent
import ru.mobileup.samples.features.form.presentation.FormComponent
import ru.mobileup.samples.features.image.presentation.ImageComponent
import ru.mobileup.samples.features.map.presentation.MapComponent
import ru.mobileup.samples.features.menu.presentation.MenuComponent
import ru.mobileup.samples.features.navigation.NavigationComponent
import ru.mobileup.samples.features.otp.presentation.OtpComponent
import ru.mobileup.samples.features.photo.presentation.PhotoComponent
import ru.mobileup.samples.features.pin_code.presentation.check_management.CheckPinCodeManagementComponent
import ru.mobileup.samples.features.pin_code.presentation.settings.PinCodeSettingsComponent
import ru.mobileup.samples.features.qr_code.presentation.QrCodeComponent
import ru.mobileup.samples.features.settings.presentation.SettingsComponent
import ru.mobileup.samples.features.shared_element_transitions.presentation.SharedElementsComponent
import ru.mobileup.samples.features.tutorial.presentation.TutorialSampleComponent
import ru.mobileup.samples.features.uploader.presentation.UploaderComponent
import ru.mobileup.samples.features.video.presentation.VideoComponent
import ru.mobileup.samples.features.work_manager.presentation.WorkManagerComponent

/**
 * A root of a Decompose component tree.
 *
 * Note: Try to minimize child count in RootComponent. It should operate by flows of screens rather than separate screens.
 */
interface RootComponent : PredictiveBackComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    val tutorialOverlayComponent: TutorialOverlayComponent

    val messageComponent: MessageComponent

    val checkPinCodeManagementComponent: CheckPinCodeManagementComponent

    val themeComponent: ThemeComponent

    sealed interface Child {
        class Menu(val component: MenuComponent) : Child
        class Form(val component: FormComponent) : Child
        class Otp(val component: OtpComponent) : Child
        class Photo(val component: PhotoComponent) : Child
        class Video(val component: VideoComponent) : Child
        class Document(val component: DocumentComponent) : Child
        class Uploader(val component: UploaderComponent) : Child
        class Calendar(val component: CalendarComponent) : Child
        class QrCode(val component: QrCodeComponent) : Child
        class Chart(val component: ChartComponent) : Child
        class Navigation(val component: NavigationComponent) : Child
        class CollapsingToolbar(val component: CollapsingToolbarComponent) : Child
        class Image(val component: ImageComponent) : Child
        class Tutorial(val component: TutorialSampleComponent) : Child
        class SharedElements(val component: SharedElementsComponent) : Child
        class PinCodeSettings(val component: PinCodeSettingsComponent) : Child
        class Map(val component: MapComponent) : Child
        class Chat(val component: ChatComponent) : Child
        class Settings(val component: SettingsComponent) : Child
        class WorkManager(val component: WorkManagerComponent) : Child
        class DivKit(val component: DivKitComponent) : Child
    }
}
