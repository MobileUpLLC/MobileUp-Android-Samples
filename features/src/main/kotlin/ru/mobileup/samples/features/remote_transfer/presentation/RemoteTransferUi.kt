package ru.mobileup.samples.features.remote_transfer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.SystemBarIconsColor
import ru.mobileup.samples.core.utils.SystemBars
import ru.mobileup.samples.core.utils.clickableNoRipple
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.remote_transfer.domain.RemoteTransferTab
import ru.mobileup.samples.features.remote_transfer.domain.toStringRes
import ru.mobileup.samples.features.remote_transfer.presentation.widgets.DownloaderContent
import ru.mobileup.samples.features.remote_transfer.presentation.widgets.UploaderContent

@Composable
fun RemoteTransferUi(
    component: RemoteTransferComponent,
    modifier: Modifier = Modifier
) {
    SystemBars(
        statusBarColor = Color.Transparent,
        statusBarIconsColor = SystemBarIconsColor.Light,
    )

    RemoteTransferContent(
        component = component,
        modifier = modifier
    )
}

@Composable
private fun RemoteTransferContent(
    component: RemoteTransferComponent,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            RemoteTransferTopBar()
        }
    ) { paddingValues ->
        val keyboardController = LocalSoftwareKeyboardController.current

        val selectedTab by component.selectedTab.collectAsState()
        val pagerState = rememberPagerState(initialPage = 0) {
            RemoteTransferTab.entries.size
        }
        val remoteTransferState by component.remoteTransferState.collectAsState()

        LaunchedEffect(selectedTab) {
            pagerState.animateScrollToPage(selectedTab.ordinal)
        }

        LaunchedEffect(pagerState.currentPage) {
            component.onTabSelect(
                if (pagerState.currentPage == 0) {
                    RemoteTransferTab.Upload
                } else {
                    RemoteTransferTab.Download
                }
            )

            if (pagerState.currentPage == 0) {
                component.linkInputControl.onFocusChange(false)
                keyboardController?.hide()
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            RemoteTransferTabRow(
                selectedTab = selectedTab,
                onTabSelect = component::onTabSelect,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                when (index) {
                    RemoteTransferTab.Upload.ordinal -> {
                        UploaderContent(
                            component = component,
                            uploaderState = remoteTransferState.uploaderState,
                            onOpenDownloader = {
                                component.onTabSelect(RemoteTransferTab.Download)
                            }
                        )
                    }

                    RemoteTransferTab.Download.ordinal -> {
                        DownloaderContent(
                            component = component,
                            downloaderState = remoteTransferState.downloaderState
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RemoteTransferTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CustomTheme.colors.palette.black)
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "logo",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = stringResource(R.string.remote_transfer_title),
            color = CustomTheme.colors.palette.white,
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RemoteTransferTabRow(
    selectedTab: RemoteTransferTab,
    onTabSelect: (RemoteTransferTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        RemoteTransferTab.entries.forEach { tab ->
            RemoteTransferTabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onTabSelect = onTabSelect,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RemoteTransferTabItem(
    tab: RemoteTransferTab,
    isSelected: Boolean,
    onTabSelect: (RemoteTransferTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickableNoRipple { onTabSelect(tab) }
    ) {
        Text(
            text = tab.toStringRes().localized(),
            style = CustomTheme.typography.button.bold,
            color = if (isSelected) {
                CustomTheme.colors.button.primary
            } else {
                CustomTheme.colors.text.secondary
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(horizontal = 4.dp)
                .background(
                    if (isSelected) {
                        CustomTheme.colors.button.primary
                    } else {
                        CustomTheme.colors.text.secondary
                    }
                )
        )
    }
}

@Preview
@Composable
private fun RemoteTransferUiPreview() {
    AppTheme {
        RemoteTransferUi(FakeRemoteTransferComponent())
    }
}