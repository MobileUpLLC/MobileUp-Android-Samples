package ru.mobileup.samples.features.optimistic_update.domain.job_launcher

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface JobLauncher<K : Any, T : Any> {
    val inProgressRequestColors: StateFlow<Set<K>>

    fun launchJob(
        key: K,
        targetState: T,
        onErrorHandled: ((e: Exception) -> Unit)? = null,
        block: suspend () -> Unit,
    ): Job
}