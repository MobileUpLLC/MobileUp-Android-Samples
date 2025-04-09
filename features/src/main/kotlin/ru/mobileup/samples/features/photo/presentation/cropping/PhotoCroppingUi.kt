package ru.mobileup.samples.features.photo.presentation.cropping

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.utils.LockScreenOrientation
import ru.mobileup.samples.features.R

@Composable
fun PhotoCroppingUi(
    component: PhotoCroppingComponent,
    modifier: Modifier = Modifier
) {

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)

    val pickedImageUri by component.pickedImageUri.collectAsState()

    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            component.onPickPhoto(it)
        }
    }

    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val cropImageView = remember {
        CropImageView(context)
            .apply {
                setImageCropOptions(
                    CropImageOptions(
                        cropShape = CropImageView.CropShape.OVAL,
                        fixAspectRatio = true,
                        guidelines = CropImageView.Guidelines.ON_TOUCH,
                        autoZoomEnabled = false,
                        scaleType = CropImageView.ScaleType.CENTER_CROP,
                    )
                )
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
    }

    LaunchedEffect(pickedImageUri) {
        cropImageView.setImageUriAsync(pickedImageUri)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .statusBarsPadding(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    enabled = pickedImageUri != null,
                    onClick = { cropImageView.rotateImage(90) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_rotate),
                        contentDescription = "rotate_image"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            pickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_gallery_picker),
                            contentDescription = "gallery_picker"
                        )
                    }

                    IconButton(
                        enabled = pickedImageUri != null,
                        onClick = { croppedBitmap = cropImageView.getCroppedImage() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_crop),
                            contentDescription = "crop_image"
                        )
                    }
                }
            }
        }
    ) {
        AndroidView(
            modifier = Modifier
                .padding(it),
            factory = { cropImageView }
        )
    }

    croppedBitmap?.let {
        Dialog(
            onDismissRequest = { croppedBitmap = null }
        ) {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "cropped_image"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPhotoCroppingUi() {
    AppTheme {
        PhotoCroppingUi(FakePhotoCroppingComponent())
    }
}
