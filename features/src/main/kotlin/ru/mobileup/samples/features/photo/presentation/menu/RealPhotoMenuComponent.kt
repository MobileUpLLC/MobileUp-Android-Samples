package ru.mobileup.samples.features.photo.presentation.menu

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.permissions.SinglePermissionResult
import ru.mobileup.samples.core.utils.componentScope

class RealPhotoMenuComponent(
    componentContext: ComponentContext,
    private val permissionService: PermissionService,
    private val onOutput: (PhotoMenuComponent.Output) -> Unit
) : ComponentContext by componentContext, PhotoMenuComponent {

    override fun onCameraClick() {
        componentScope.launch {
            val permissionsResult = permissionService.requestPermission(
                android.Manifest.permission.CAMERA
            )
            if (permissionsResult is SinglePermissionResult.Granted) {
                onOutput(PhotoMenuComponent.Output.CameraRequested)
            }
        }
    }

    override fun onPreviewClick(uris: List<Uri>) {
        onOutput(PhotoMenuComponent.Output.PreviewRequested(uris))
    }

    override fun onCroppingClick() {
        onOutput(PhotoMenuComponent.Output.CroppingRequested)
    }
}