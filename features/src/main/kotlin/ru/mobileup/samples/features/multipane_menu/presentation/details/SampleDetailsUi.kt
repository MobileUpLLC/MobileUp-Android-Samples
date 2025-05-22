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
fun SampleDetailsUi(
    component: SampleDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.childStack.collectAsState()

    Children(
        modifier = modifier,
        stack = stack,
    ) { child ->
        when (val instance = child.instance) {
            is SampleDetailsComponent.Child.Form -> FormUi(instance.component)
            is SampleDetailsComponent.Child.Otp -> OtpUi(instance.component)
            is SampleDetailsComponent.Child.Photo -> PhotoUi(instance.component)
            is SampleDetailsComponent.Child.Video -> VideoUi(instance.component)
            is SampleDetailsComponent.Child.Document -> DocumentUi(instance.component)
            is SampleDetailsComponent.Child.RemoteTransfer -> RemoteTransferUi(instance.component)
            is SampleDetailsComponent.Child.Calendar -> CalendarUi(instance.component)
            is SampleDetailsComponent.Child.QrCode -> QrCodeUi(instance.component)
            is SampleDetailsComponent.Child.Chart -> ChartUi(instance.component)
            is SampleDetailsComponent.Child.Navigation -> NavigationUi(instance.component)
            is SampleDetailsComponent.Child.CollapsingToolbar -> CollapsingToolbarUi(instance.component)
            is SampleDetailsComponent.Child.Image -> ImageUi(instance.component)
            is SampleDetailsComponent.Child.Tutorial -> TutorialSampleUi(instance.component)
            is SampleDetailsComponent.Child.SharedElements -> SharedElementsUi(instance.component)
            is SampleDetailsComponent.Child.PinCodeSettings -> PinCodeSettingsUi(instance.component)
            is SampleDetailsComponent.Child.Map -> MapUi(instance.component)
            is SampleDetailsComponent.Child.Chat -> ChatUi(instance.component)
            is SampleDetailsComponent.Child.WorkManager -> WorkManagerUi(instance.component)
            is SampleDetailsComponent.Child.DivKit -> DivKitUi(instance.component)
        }
    }
}
