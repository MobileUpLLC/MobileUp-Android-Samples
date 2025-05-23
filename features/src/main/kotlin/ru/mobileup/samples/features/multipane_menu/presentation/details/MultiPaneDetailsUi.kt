package ru.mobileup.samples.features.multipane_menu.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.features.calendar.presentation.CalendarUi
import ru.mobileup.samples.features.charts.presentation.ChartUi
import ru.mobileup.samples.features.chat.presentation.ChatUi
import ru.mobileup.samples.features.collapsing_toolbar.presentation.CollapsingToolbarUi
import ru.mobileup.samples.features.divkit.presentation.DivKitUi
import ru.mobileup.samples.features.document.presentation.DocumentUi
import ru.mobileup.samples.features.form.presentation.FormUi
import ru.mobileup.samples.features.image.presentation.ImageUi
import ru.mobileup.samples.features.map.presentation.MapUi
import ru.mobileup.samples.features.navigation.NavigationUi
import ru.mobileup.samples.features.otp.presentation.OtpUi
import ru.mobileup.samples.features.photo.presentation.PhotoUi
import ru.mobileup.samples.features.pin_code.presentation.settings.PinCodeSettingsUi
import ru.mobileup.samples.features.qr_code.presentation.QrCodeUi
import ru.mobileup.samples.features.remote_transfer.presentation.RemoteTransferUi
import ru.mobileup.samples.features.shared_element_transitions.presentation.SharedElementsUi
import ru.mobileup.samples.features.tutorial.presentation.TutorialSampleUi
import ru.mobileup.samples.features.video.presentation.VideoUi
import ru.mobileup.samples.features.work_manager.presentation.WorkManagerUi

@Composable
fun MultiPaneDetailsUi(
    component: MultiPaneDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.childStack.collectAsState()

    Children(
        modifier = modifier,
        stack = stack,
    ) { child ->
        when (val instance = child.instance) {
            is MultiPaneDetailsComponent.Child.Form -> FormUi(instance.component)
            is MultiPaneDetailsComponent.Child.Otp -> OtpUi(instance.component)
            is MultiPaneDetailsComponent.Child.Photo -> PhotoUi(instance.component)
            is MultiPaneDetailsComponent.Child.Video -> VideoUi(instance.component)
            is MultiPaneDetailsComponent.Child.Document -> DocumentUi(instance.component)
            is MultiPaneDetailsComponent.Child.RemoteTransfer -> RemoteTransferUi(instance.component)
            is MultiPaneDetailsComponent.Child.Calendar -> CalendarUi(instance.component)
            is MultiPaneDetailsComponent.Child.QrCode -> QrCodeUi(instance.component)
            is MultiPaneDetailsComponent.Child.Chart -> ChartUi(instance.component)
            is MultiPaneDetailsComponent.Child.Navigation -> NavigationUi(instance.component)
            is MultiPaneDetailsComponent.Child.CollapsingToolbar -> CollapsingToolbarUi(instance.component)
            is MultiPaneDetailsComponent.Child.Image -> ImageUi(instance.component)
            is MultiPaneDetailsComponent.Child.Tutorial -> TutorialSampleUi(instance.component)
            is MultiPaneDetailsComponent.Child.SharedElements -> SharedElementsUi(instance.component)
            is MultiPaneDetailsComponent.Child.PinCodeSettings -> PinCodeSettingsUi(instance.component)
            is MultiPaneDetailsComponent.Child.Map -> MapUi(instance.component)
            is MultiPaneDetailsComponent.Child.Chat -> ChatUi(instance.component)
            is MultiPaneDetailsComponent.Child.WorkManager -> WorkManagerUi(instance.component)
            is MultiPaneDetailsComponent.Child.DivKit -> DivKitUi(instance.component)
        }
    }
}
