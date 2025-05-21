package ru.mobileup.samples.features.work_manager.presentation.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mobileup.samples.core.theme.custom.CustomTheme
import ru.mobileup.samples.features.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NextExecutionTimeInfo(timeMillis: Long?, modifier: Modifier = Modifier) {
    Crossfade(modifier = modifier, targetState = timeMillis) {
        if (it != null) {
            val formatted =
                SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault()).format(Date(it))

            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.work_task_enqueued),
                        style = CustomTheme.typography.title.regular
                    )

                    Text(
                        text = stringResource(R.string.work_next_execution_time),
                    )

                    Text(
                        text = formatted.replace(" ", "\n"),
                        style = CustomTheme.typography.body.regular
                    )
                }
            }
        }
    }
}
