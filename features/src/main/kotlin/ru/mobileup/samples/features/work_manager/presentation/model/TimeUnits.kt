package ru.mobileup.samples.features.work_manager.presentation.model

import androidx.annotation.StringRes
import ru.mobileup.samples.features.R

interface TimeUnitWithRange {
    val range: LongRange
}

enum class DelayTimeUnit : TimeUnitWithRange {
    SECONDS, MINUTES, HOURS, DAYS;

    override val range: LongRange
        get() = when (this) {
            SECONDS, MINUTES -> 0L..59L
            HOURS -> 0L..23L
            DAYS -> 0L..24L
        }

    val resId: Int
        @StringRes
        get() = when (this) {
            SECONDS -> R.string.work_seconds
            MINUTES -> R.string.work_minutes
            HOURS -> R.string.work_hours
            DAYS -> R.string.work_days
        }

    val pluralResId: Int
        @StringRes
        get() = when (this) {
            SECONDS -> R.plurals.work_seconds_plural
            MINUTES -> R.plurals.work_minutes_plural
            HOURS -> R.plurals.work_hours_plural
            DAYS -> R.plurals.work_days_plural
        }
}

enum class IntervalTimeUnit : TimeUnitWithRange {
    MINUTES, HOURS, DAYS;

    override val range: LongRange
        get() = when (this) {
            MINUTES -> 15L..59L
            HOURS -> 1L..23L
            DAYS -> 1L..30L
        }

    val resId: Int
        @StringRes
        get() = when (this) {
            MINUTES -> R.string.work_minutes
            HOURS -> R.string.work_hours
            DAYS -> R.string.work_days
        }

    val pluralResId: Int
        @StringRes
        get() = when (this) {
            MINUTES -> R.plurals.work_minutes_plural
            HOURS -> R.plurals.work_hours_plural
            DAYS -> R.plurals.work_days_plural
        }
}
