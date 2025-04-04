package ru.mobileup.samples.features.root.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import ru.mobileup.samples.core.message.presentation.MessageUi
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.tutorial.presentation.overlay.TutorialOverlayUi
import ru.mobileup.samples.core.utils.ConfigureSystemBars
import ru.mobileup.samples.core.utils.LocalSystemBarsSettings
import ru.mobileup.samples.core.utils.accumulate
import ru.mobileup.samples.features.calendar.presentation.CalendarUi
import ru.mobileup.samples.features.charts.presentation.ChartUi
import ru.mobileup.samples.features.collapsing_toolbar.presentation.CollapsingToolbarUi
import ru.mobileup.samples.features.document.presentation.DocumentUi
import ru.mobileup.samples.features.form.presentation.FormUi
import ru.mobileup.samples.features.image.presentation.ImageUi
import ru.mobileup.samples.features.menu.presentation.MenuUi
import ru.mobileup.samples.features.navigation.NavigationUi
import ru.mobileup.samples.features.otp.presentation.OtpUi
import ru.mobileup.samples.features.photo.presentation.PhotoUi
import ru.mobileup.samples.features.qr_code.presentation.QrCodeUi
import ru.mobileup.samples.features.shared_element_transitions.presentation.SharedElementsUi
import ru.mobileup.samples.features.tutorial.presentation.TutorialSampleUi
import ru.mobileup.samples.features.uploader.presentation.UploaderUi
import ru.mobileup.samples.features.video.presentation.VideoUi

@Composable
fun RootUi(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()
    val systemBarsSettings = LocalSystemBarsSettings.current.accumulate()

    Children(childStack, modifier) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Menu -> MenuUi(instance.component)
            is RootComponent.Child.Form -> FormUi(instance.component)
            is RootComponent.Child.Otp -> OtpUi(instance.component)
            is RootComponent.Child.Photo -> PhotoUi(instance.component)
            is RootComponent.Child.Video -> VideoUi(instance.component)
            is RootComponent.Child.Document -> DocumentUi(instance.component)
            is RootComponent.Child.Uploader -> UploaderUi(instance.component)
            is RootComponent.Child.Calendar -> CalendarUi(instance.component)
            is RootComponent.Child.QrCode -> QrCodeUi(instance.component)
            is RootComponent.Child.Chart -> ChartUi(instance.component)
            is RootComponent.Child.Navigation -> NavigationUi(instance.component)
            is RootComponent.Child.CollapsingToolbar -> CollapsingToolbarUi(instance.component)
            is RootComponent.Child.Image -> ImageUi(instance.component)
            is RootComponent.Child.Tutorial -> TutorialSampleUi(instance.component)
            is RootComponent.Child.SharedElements -> SharedElementsUi(instance.component)
        }
    }

    TutorialOverlayUi(component.tutorialOverlayComponent)

    MessageUi(
        component = component.messageComponent,
        bottomPadding = 16.dp
    )

    ConfigureSystemBars(systemBarsSettings)
}

@Preview(showSystemUi = true)
@Composable
private fun RootUiPreview() {
    AppTheme {
        RootUi(FakeRootComponent())
    }
}