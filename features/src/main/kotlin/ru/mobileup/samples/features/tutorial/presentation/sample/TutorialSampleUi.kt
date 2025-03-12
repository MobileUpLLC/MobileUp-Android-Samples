package ru.mobileup.samples.features.tutorial.presentation.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.strResDesc
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.core.utils.dispatchOnBackPressed
import ru.mobileup.samples.features.R
import ru.mobileup.samples.features.tutorial.domain.TutorialFilter
import ru.mobileup.samples.features.tutorial.domain.TutorialManager
import ru.mobileup.samples.features.tutorial.presentation.highlightableItem
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.TutorialMessageKeys

@Composable
fun TutorialSampleUi(
    component: TutorialSampleComponent,
    modifier: Modifier = Modifier
) {

    val items by component.items.collectAsState()
    val selectedFilter by component.selectedFilter.collectAsState()
    val availableFilters by component.availableFilters.collectAsState()

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        topBar = {
            Toolbar(
                tutorialManager = component
                    .tutorialManagementComponent
                    .tutorialManager,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            FiltersSection(
                availableFilters = availableFilters,
                selectedFilter = selectedFilter,
                onFilterSelect = component::onFilterSelected,
                tutorialManager = component
                    .tutorialManagementComponent
                    .tutorialManager
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                items(items) {
                    Text(
                        text = it.toString(),
                        style = CustomTheme.typography.button.bold,
                        color = CustomTheme.colors.text.invert,
                        modifier = Modifier
                            .background(
                                color = CustomTheme.colors.button.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FiltersSection(
    availableFilters: List<TutorialFilter>,
    selectedFilter: TutorialFilter,
    onFilterSelect: (TutorialFilter) -> Unit,
    tutorialManager: TutorialManager,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableFilters.forEach { filter ->
            Text(
                text = filter.displayName.localized(),
                style = CustomTheme.typography.button.bold,
                color = if (filter == selectedFilter) {
                    CustomTheme.colors.text.invert
                } else {
                    CustomTheme.colors.text.primary
                },
                modifier = Modifier
                    .background(
                        color = if (filter == selectedFilter) {
                            CustomTheme.colors.text.primary
                        } else {
                            CustomTheme.colors.text.invert
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onFilterSelect(filter) }
                    .highlightableItem(
                        key = filter.toTutorialMsssageKey(),
                        tutorialManager = tutorialManager
                    )
                    .padding(12.dp)
            )
        }
    }
}

@Composable
private fun Toolbar(
    tutorialManager: TutorialManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(modifier = modifier.fillMaxWidth()) {
        IconButton(
            onClick = { dispatchOnBackPressed(context) },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .highlightableItem(
                    key = TutorialMessageKeys.Back,
                    tutorialManager = tutorialManager
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null
            )
        }

        Text(
            text = stringResource(id = R.string.tutorial_toolbar_title),
            style = CustomTheme.typography.title.regular,
            color = CustomTheme.colors.text.primary,
            modifier = Modifier
                .align(Alignment.Center)
                .highlightableItem(
                    key = TutorialMessageKeys.Title,
                    tutorialManager = tutorialManager
                )
                .padding(8.dp)
        )
    }
}

val TutorialFilter.displayName: StringDesc
    get() = when (this) {
        TutorialFilter.All -> R.string.tutorial_filter_all.strResDesc()
        TutorialFilter.First -> R.string.tutorial_filter_first.strResDesc()
        TutorialFilter.Second -> R.string.tutorial_filter_second.strResDesc()
        TutorialFilter.Third -> R.string.tutorial_filter_third.strResDesc()
    }

fun TutorialFilter.toTutorialMsssageKey() = when (this) {
    TutorialFilter.All -> TutorialMessageKeys.All
    TutorialFilter.First -> TutorialMessageKeys.First
    TutorialFilter.Second -> TutorialMessageKeys.Second
    TutorialFilter.Third -> TutorialMessageKeys.Third
}
