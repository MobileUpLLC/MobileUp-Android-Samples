package ru.mobileup.samples.features.tutorial.presentation.sample

import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.features.tutorial.domain.TutorialFilter
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.TutorialManagementSampleComponent

interface TutorialSampleComponent {
    val items: StateFlow<List<Int>>

    val selectedFilter: StateFlow<TutorialFilter>
    val availableFilters: StateFlow<List<TutorialFilter>>

    val tutorialManagementComponent: TutorialManagementSampleComponent

    fun onFilterSelected(filter: TutorialFilter)
}