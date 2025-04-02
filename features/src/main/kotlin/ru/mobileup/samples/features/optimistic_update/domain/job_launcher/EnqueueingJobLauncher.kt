package ru.mobileup.samples.features.optimistic_update.domain.job_launcher

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch

/*
    If multiple actions are requested with one key, this impl
    is waiting for already working ones to be completed before starting a new job
 */
class EnqueueingJobLauncher<K : Any, T : Any>(
    private val errorHandler: ErrorHandler,
    private val dropQueueIfAnyFailed: Boolean
) : JobLauncher<K, T> {

    private val mutexMap = mutableMapOf<K, Mutex>()
    private val jobMap = mutableMapOf<K, Pair<Deferred<*>, T>>()

    private val coroutineScope = MainScope()

    private val _inProgressRequestColors = MutableStateFlow(emptySet<K>())
    override val inProgressRequestColors = _inProgressRequestColors.asStateFlow()

    override fun launchJob(
        key: K,
        targetState: T,
        onErrorHandled: ((e: Exception) -> Unit)?,
        block: suspend () -> Unit,
    ) = coroutineScope.safeLaunch(
        errorHandler = errorHandler,
        onErrorHandled = onErrorHandled
    ) innerLaunch@{
        do {
            // Current working job with this key
            val (workingJob, workingTarget) = jobMap[key] ?: (null to null)

            try {
                when (dropQueueIfAnyFailed) {
                    // If workingJob completes with error, throws it
                    true -> workingJob?.await()
                    // If workingJob completes with error, doesn't throw it
                    false -> workingJob?.join()
                }
            } catch (e: Exception) {
                // Exception will be processed in parent coroutine of workingJob
                return@innerLaunch
            }

            // Check whether joined work is same and successfully done
            if (workingTarget == targetState && workingJob?.isCancelled == false) return@innerLaunch

            val mutex = mutexMap.getOrPut(key, ::Mutex)
            if (!mutex.tryLock()) continue

            try {
                coroutineScope {
                    val currentJob = async(start = CoroutineStart.LAZY) {
                        block()
                    }

                    _inProgressRequestColors.update { it + key }
                    jobMap[key] = currentJob to targetState

                    currentJob.await()
                }
            } finally {
                mutexMap.remove(key)?.unlock()
                _inProgressRequestColors.update { it - key }
                jobMap.remove(key)
            }

            break
        } while (true)
    }
}
