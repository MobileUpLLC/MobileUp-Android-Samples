package ru.mobileup.samples.features.work_manager.presentation.model

import androidx.annotation.StringRes
import ru.mobileup.samples.features.R

enum class TaskType {
    PERIODIC, ONE_TIME;

    val resId: Int
        @StringRes
        get() = when (this) {
            PERIODIC -> R.string.work_task_type_periodic
            ONE_TIME -> R.string.work_task_type_one_time
        }
}
