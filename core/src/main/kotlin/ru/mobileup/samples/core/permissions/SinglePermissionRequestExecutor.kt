package ru.mobileup.samples.core.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mobileup.samples.core.activity.ActivityProvider

internal class SinglePermissionRequestExecutor(
    private val activityProvider: ActivityProvider
) {

    companion object {
        private const val AUTOMATIC_THRESHOLD = 500
    }

    private var activityResultLauncher = MutableStateFlow<ActivityResultLauncher<String>?>(null)
    private val permissionsResultFlow = MutableSharedFlow<Boolean?>()

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
            activityProvider.activityStateFlow.collect {
                if (it != null) {
                    registerLauncher(it)
                } else {
                    unregisterLauncher()
                }
            }
        }
    }

    suspend fun process(permission: String): SinglePermissionResult {
        val startTime = System.currentTimeMillis()
        activityResultLauncher.filterNotNull().first().launch(permission)
        val granted = permissionsResultFlow.first() ?: throw CancellationException()
        return if (granted) {
            SinglePermissionResult.Granted
        } else {
            val rationale =
                activityProvider.awaitActivity().shouldShowRequestPermissionRationale(permission)
            val endTime = System.currentTimeMillis()
            val automatically = endTime - startTime < AUTOMATIC_THRESHOLD
            SinglePermissionResult.Denied(
                permanently = !rationale,
                automatically = automatically
            )
        }
    }

    private fun registerLauncher(activity: ComponentActivity) {
        activityResultLauncher.value =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                activity.lifecycleScope.launch {
                    permissionsResultFlow.emit(it)
                }
            }
    }

    private suspend fun unregisterLauncher() {
        activityResultLauncher.value?.unregister()
        activityResultLauncher.value = null
        permissionsResultFlow.emit(null)
    }
}
