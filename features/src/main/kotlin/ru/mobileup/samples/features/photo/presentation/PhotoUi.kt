package ru.mobileup.samples.features.photo.presentation

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.utils.LockScreenOrientation
import ru.mobileup.samples.features.photo.presentation.camera.PhotoCameraUi
import ru.mobileup.samples.features.photo.presentation.cropping.PhotoCroppingUi
import ru.mobileup.samples.features.photo.presentation.menu.PhotoMenuUi
import ru.mobileup.samples.features.photo.presentation.preview.PhotoPreviewUi

@Composable
fun PhotoUi(
    component: PhotoComponent,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.collectAsState()

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)

    Children(
        stack = childStack,
        animation = stackAnimation(slide()),
        modifier = modifier
    ) { child ->
        when (val instance = child.instance) {
            is PhotoComponent.Child.Menu -> PhotoMenuUi(instance.component)
            is PhotoComponent.Child.Camera -> PhotoCameraUi(instance.component)
            is PhotoComponent.Child.Preview -> PhotoPreviewUi(instance.component)
            is PhotoComponent.Child.Cropping -> PhotoCroppingUi(instance.component)
        }
    }
}

@Preview
@Composable
private fun VideoUiPreview() {
    AppTheme {
        PhotoUi(FakePhotoComponent())
    }
}