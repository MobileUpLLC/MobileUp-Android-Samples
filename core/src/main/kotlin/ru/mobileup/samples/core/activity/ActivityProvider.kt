package ru.mobileup.samples.core.activity

import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class ActivityProvider {

    private val activityStateMutableFlow = MutableStateFlow<FragmentActivity?>(null)

    val activityStateFlow: StateFlow<FragmentActivity?>
        get() = activityStateMutableFlow

    val activity: FragmentActivity? get() = activityStateMutableFlow.value

    fun attachActivity(activity: FragmentActivity) {
        activityStateMutableFlow.value = activity
    }

    fun detachActivity() {
        activityStateMutableFlow.value = null
    }

    suspend fun awaitActivity(): FragmentActivity {
        return activityStateMutableFlow.filterNotNull().first()
    }
}
