package ru.mobileup.samples.features.optimistic_update.domain.job_launcher

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import kotlin.coroutines.coroutineContext

/*
    If multiple actions are requested with one key, this impl
    is cancelling the latest one (if it isn't doing same work) and launching a new job
 */
class CancellingJobLauncher<K : Any, T : Any>(
    private val errorHandler: ErrorHandler
) : JobLauncher<K, T> {

    private val coroutineScope = MainScope()

    private val jobMap = mutableMapOf<K, Pair<Job, T>>()

    private val _inProgressRequestIds = MutableStateFlow(emptySet<K>())
    override val inProgressRequestColors = _inProgressRequestIds.asStateFlow()

    override fun launchJob(
        key: K,
        targetState: T,
        onErrorHandled: ((e: Exception) -> Unit)?,
        block: suspend () -> Unit
    ): Job {
        // Checking if we are working with that color at the moment
        val currentJobAndTarget = jobMap[key]
        if (currentJobAndTarget != null) {
            val (currentJob, currentTarget) = currentJobAndTarget
            if (currentTarget == targetState && currentJob.isActive) {
                // Current active job is with the same target, no need to start a new one
                return currentJob
            } else if (currentTarget != targetState) {
                // Current job is with different target, cancel and start a new one
                currentJob.cancel()
            }
        }

        _inProgressRequestIds.update { it + key }

        val job = coroutineScope.safeLaunch(
            errorHandler = errorHandler,
            onErrorHandled = onErrorHandled
        ) {
            try {
                block()
            } finally {
                if (jobMap[key]?.first == coroutineContext.job) {
                    _inProgressRequestIds.update { it - key }
                    jobMap.remove(key)
                }
            }
        }

        jobMap[key] = job to targetState

        return job
    }
}