package ru.mobileup.samples.features.photo.presentation.preview

import android.net.Uri
import android.os.Build
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.dialog.standard.DialogButton
import ru.mobileup.samples.core.dialog.standard.StandardDialogData
import ru.mobileup.samples.core.dialog.standard.standardDialogControl
import ru.mobileup.samples.core.media.ImageResource
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.permissions.PermissionService
import ru.mobileup.samples.core.permissions.SinglePermissionResult
import ru.mobileup.samples.core.sharing.data.SharingService
import ru.mobileup.samples.core.utils.Resource
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.image.createImageCarouselComponent
import ru.mobileup.samples.features.image.presentation.carousel.ImageCarouselComponent
import ru.mobileup.samples.features.photo.data.PhotoFileManager

class RealPhotoPreviewComponent(
    private val uris: List<Uri>,
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val photoFileManager: PhotoFileManager,
    private val sharingService: SharingService,
    private val permissionService: PermissionService,
    private val messageService: MessageService
) : ComponentContext by componentContext, PhotoPreviewComponent {

    override val imageCarouselComponent: ImageCarouselComponent =
        componentFactory.createImageCarouselComponent(
            uris.map { ImageResource(it) },
            childContext("imageCarousel")
        )

    override val saveDialog = standardDialogControl("saveDialog")

    override fun onSaveClick() = saveDialog.show(
        StandardDialogData(
            title = R.string.save_photo_dialog_title.strResDesc(),
            message = R.string.save_photo_dialog_message.strResDesc(),
            confirmButton = DialogButton(
                text = R.string.photo_confirm_btn.strResDesc(),
                action = {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        componentScope.launch {
                            val permissionsResult = permissionService.requestPermission(
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                            if (permissionsResult is SinglePermissionResult.Granted) {
                                savePhoto()
                            }
                        }
                    } else {
                        savePhoto()
                    }
                }
            ),
            dismissButton = DialogButton(
                text = R.string.photo_dismiss_btn.strResDesc(),
                action = saveDialog::dismiss
            )
        )
    )

    override fun onShareClick() {
        uris.getOrNull(imageCarouselComponent.imageCarousel.value.currentImagePosition)
            ?.let { uri ->
                sharingService.shareMedia(
                    uri = uri,
                    mimeType = "image/*"
                )
            }
    }

    private fun savePhoto() {
        componentScope.launch {
            uris.getOrNull(imageCarouselComponent.imageCarousel.value.currentImagePosition)?.let {
                val result = photoFileManager.movePhotoToMediaStore(it)

                messageService.showMessage(
                    Message(
                        text = StringDesc.Resource(
                            if (result == null) {
                                R.string.photo_save_failed
                            } else {
                                R.string.photo_saved
                            }
                        )
                    )
                )
            }
        }
    }
}