package ru.mobileup.samples.features.tutorial.presentation.sample

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.tutorial.domain.TutorialFilter
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.FakeTutorialManagementSampleComponent
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.TutorialManagementSampleComponent

class FakeTutorialSampleComponent : TutorialSampleComponent {

    override val items: StateFlow<List<Int>> =
        MutableStateFlow(List(5) { it })

    override val selectedFilter: StateFlow<TutorialFilter> =
        MutableStateFlow(TutorialFilter.All)

    override val availableFilters: StateFlow<List<TutorialFilter>> =
        MutableStateFlow(TutorialFilter.entries
        )
    override val tutorialManagementComponent: TutorialManagementSampleComponent =
        FakeTutorialManagementSampleComponent()

    override fun onFilterSelected(filter: TutorialFilter) = Unit
}