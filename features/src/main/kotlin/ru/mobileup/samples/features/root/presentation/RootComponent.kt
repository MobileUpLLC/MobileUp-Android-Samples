package ru.mobileup.samples.features.root.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.message.presentation.MessageComponent
import ru.mobileup.samples.features.calendar.presentation.CalendarComponent
import ru.mobileup.samples.features.charts.presentation.ChartComponent
import ru.mobileup.samples.features.form.presentation.FormComponent
import ru.mobileup.samples.features.menu.presentation.MenuComponent
import ru.mobileup.samples.features.navigation.NavigationComponent
import ru.mobileup.samples.features.otp.presentation.OtpComponent
import ru.mobileup.samples.features.qr_code.presentation.QrCodeComponent
import ru.mobileup.samples.features.video.presentation.VideoComponent

/**
 * A root of a Decompose component tree.
 *
 * Note: Try to minimize child count in RootComponent. It should operate by flows of screens rather than separate screens.
 */
interface RootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    val messageComponent: MessageComponent

    sealed interface Child {
        class Menu(val component: MenuComponent) : Child
        class Form(val component: FormComponent) : Child
        class Otp(val component: OtpComponent) : Child
        class Video(val component: VideoComponent) : Child
        class Calendar(val component: CalendarComponent) : Child
        class QrCode(val component: QrCodeComponent) : Child
        class Chart(val component: ChartComponent) : Child
        class Navigation(val component: NavigationComponent) : Child
    }
}
