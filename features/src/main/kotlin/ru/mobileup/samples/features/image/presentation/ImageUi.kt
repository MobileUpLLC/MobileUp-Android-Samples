package ru.mobileup.samples.features.image.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.dispatchOnBackPressed
import ru.mobileup.samples.features.image.presentation.carousel.EmbeddedImageCarouselUi
import ru.mobileup.samples.features.image.presentation.carousel.FullScreenImageCarouselUi
import ru.mobileup.samples.features.image.presentation.carousel.ImageCarouselMode

@Composable
fun ImageUi(
    component: ImageComponent,
    modifier: Modifier = Modifier,
) {
    val mode by component.imageCarouselComponent.mode.collectAsState()
    val title by component.title.collectAsState()
    val description by component.description.collectAsState()

    Box(modifier = modifier) {
        when (mode) {
            ImageCarouselMode.Embedded -> {
                Column(Modifier.statusBarsPadding()) {
                    EmbeddedImageCarouselUi(component.imageCarouselComponent)
                    CatsTextContent(
                        title = title.localized(),
                        description = description.localized()
                    )
                }
            }

            ImageCarouselMode.Fullscreen -> {
                FullScreenImageCarouselUi(
                    component = component.imageCarouselComponent,
                    modifier = Modifier.background(CustomTheme.colors.background.screen)
                )
            }
        }

        ImageToolbar()
    }
}

@Composable
private fun ImageToolbar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .size(40.dp)
                .clickable { dispatchOnBackPressed(context) },
            shape = RoundedCornerShape(12.dp),
            color = CustomTheme.colors.background.screen
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.requiredSize(24.dp)
            )
        }
    }
}

@Composable
private fun CatsTextContent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        Text(
            text = title,
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = description,
            style = CustomTheme.typography.body.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun ImageCarouselUiPreview() {
    AppTheme {
        ImageUi(FakeImageComponent())
    }
}