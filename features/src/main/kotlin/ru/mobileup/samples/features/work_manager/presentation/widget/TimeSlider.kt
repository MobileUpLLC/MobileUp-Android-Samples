package ru.mobileup.samples.features.work_manager.presentation.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.features.work_manager.presentation.model.TimeUnitWithRange

@Composable
fun <T> TimeSlider(
    label: String,
    value: Long,
    unit: T,
    onValueChange: (Long) -> Unit,
    onUnitChange: (T) -> Unit,
    allUnits: List<T>,
    getRange: (T) -> LongRange,
    getPluralResId: (T) -> Int,
    getResId: (T) -> Int,
    modifier: Modifier = Modifier,
) where T : Enum<T>, T : TimeUnitWithRange {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "$label:\n${
                    pluralStringResource(
                        getPluralResId(unit),
                        value.toInt(),
                        value
                    )
                }"
            )
            DropdownMenuSelector(
                selected = unit,
                options = allUnits,
                onSelect = onUnitChange,
                displayResId = getResId
            )
        }

        val range = remember(unit) { getRange(unit) }

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toLong()) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = (range.last - range.first).toInt().coerceAtLeast(0)
        )
    }
}
