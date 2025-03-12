package ru.mobileup.samples.features.tutorial.presentation.sample

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import ru.mobileup.samples.features.tutorial.presentation.sample.tutorial.TutorialManagementSampleComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.features.tutorial.createTutorialManagementSampleComponent
import ru.mobileup.samples.features.tutorial.domain.TutorialFilter

class RealTutorialSampleComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
) : ComponentContext by componentContext, TutorialSampleComponent {

    override val selectedFilter = MutableStateFlow(TutorialFilter.All)

    override val availableFilters = MutableStateFlow(TutorialFilter.entries)

    private val unfilteredItems = MutableStateFlow(List(100) { it })

    override val items: StateFlow<List<Int>> =
        computed(unfilteredItems, selectedFilter) { items, filter ->
            when (filter) {
                TutorialFilter.All -> items
                TutorialFilter.First -> items.filter { it % 3 == 0 }
                TutorialFilter.Second -> items.filter { it % 3 == 1 }
                TutorialFilter.Third -> items.filter { it % 3 == 2 }
            }
        }

    override val tutorialManagementComponent: TutorialManagementSampleComponent =
        componentFactory.createTutorialManagementSampleComponent(
            childContext("tutorialManagement")
        )

    override fun onFilterSelected(filter: TutorialFilter) {
        selectedFilter.value = filter
    }
}