package ru.mobileup.samples.features.settings.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.app_settings.domain.AppSettings
import ru.mobileup.samples.core.app_settings.domain.AppSettingsState
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R

@Composable
fun SettingsUi(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    val settings by component.settings.collectAsState()

    Box(
        modifier = modifier
            .statusBarsPadding()
            .padding(vertical = 32.dp)
    ) {
        when (val state = settings) {
            AppSettingsState.Uninitialized -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AppSettingsState.Initialized -> {
                SettingsContent(
                    modifier = Modifier.fillMaxSize(),
                    settings = state.value,
                    onThemeClick = component::onThemeClick,
                )
            }
        }
    }
}

@Composable
private fun SettingsContent(
    settings: AppSettings,
    onThemeClick: (AppTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        ThemeItem(
            theme = settings.theme,
            onThemeClick = onThemeClick,
        )
    }
}

@Composable
private fun ThemeItem(
    theme: AppTheme,
    onThemeClick: (AppTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val animateRotation by animateFloatAsState(if (isExpanded) -180f else 0f)

    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = stringResource(R.string.settings_theme)
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CustomTheme.colors.border.primary, RoundedCornerShape(16.dp))
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(theme.name)
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.graphicsLayer { rotationZ = animateRotation },
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(isExpanded) {
            Column {
                Spacer(Modifier.height(8.dp))
                AppTheme.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.name, style = CustomTheme.typography.body.regular)
                                Spacer(Modifier.weight(1f))
                                if (it == theme) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                        onClick = {
                            onThemeClick(it)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsUiPreview() {
    AppTheme {
        SettingsUi(FakeSettingsComponent())
    }
}
