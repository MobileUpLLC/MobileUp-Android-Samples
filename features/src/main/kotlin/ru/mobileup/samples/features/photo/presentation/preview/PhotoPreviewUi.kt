package ru.mobileup.samples.features.photo.presentation.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import ru.mobileup.samples.core.dialog.standard.StandardDialog
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.image.presentation.carousel.FullScreenImageCarouselUi

@Composable
fun PhotoPreviewUi(
    component: PhotoPreviewComponent,
    modifier: Modifier = Modifier
) {
    SystemBars(
        statusBarColor = Color.Transparent,
        navigationBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
        navigationBarIconsColor = SystemBarIconsColor.Light
    )

    StandardDialog(component.saveDialog)

    PhotoPreviewContent(
        component = component,
        modifier = modifier
    )
}

@Composable
private fun PhotoPreviewContent(
    component: PhotoPreviewComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PreviewTopBar(
                onSaveClick = component::onSaveClick
            )
        }
    ) { paddingValues ->
        FullScreenImageCarouselUi(
            component = component.imageCarouselComponent,
            modifier = Modifier
                .background(CustomTheme.colors.palette.grayscale.l900)
                .padding(paddingValues)
        )
    }
}

@Composable
private fun PreviewTopBar(
    onSaveClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.palette.black)
            .run {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    padding(horizontal = 8.dp, vertical = 24.dp)
                } else {
                    padding(horizontal = 24.dp, vertical = 8.dp)
                }
            }
            .padding(top = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.photo_menu_item_preview),
            color = CustomTheme.colors.text.invert,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = "download",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable {
                    onSaveClick()
                }
        )
    }
}

@UnstableApi
@Preview
@Composable
private fun PhotoPreviewUiPreview() {
    AppTheme {
        PhotoPreviewUi(FakePhotoPreviewComponent())
    }
}