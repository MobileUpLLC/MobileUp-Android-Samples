package ru.mobileup.samples.features.video.presentation.player.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.video.domain.PlayerConfig
import ru.mobileup.samples.features.video.presentation.widgets.SlideAnimation

@Composable
fun BoxScope.PlayerCropSelector(
    playerConfig: PlayerConfig,
    onCompleteClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SlideAnimation(
        modifier = modifier.align(Alignment.BottomStart),
        isVisible = playerConfig == PlayerConfig.Crop
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.ic_cancel),
                contentDescription = "cancel",
                tint = Color.Unspecified,
                modifier = Modifier
                    .background(CustomTheme.colors.palette.black50)
                    .padding(12.dp)
                    .clickable {
                        onResetClick()
                    }
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "done",
                tint = Color.Unspecified,
                modifier = Modifier
                    .background(CustomTheme.colors.palette.black50)
                    .padding(12.dp)
                    .clickable {
                        onCompleteClick()
                    }
            )
        }
    }
}

@Preview
@Composable
private fun PlayerCropSelectorPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            PlayerCropSelector(
                playerConfig = PlayerConfig.Crop,
                onCompleteClick = { },
                onResetClick = { }
            )
        }
    }
}