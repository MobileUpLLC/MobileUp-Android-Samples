package ru.mobileup.samples.features.optimistic_update.domain.job_launcher

import ru.mobileup.samples.core.error_handling.ErrorHandler
import kotlin.reflect.KClass

class JobLauncherStore private constructor(
    private val errorHandler: ErrorHandler
) {
    companion object {
        private val lock = Any()
        private var instance: JobLauncherStore? = null

        fun getOrCreateInstance(errorHandler: ErrorHandler): JobLauncherStore {
            instance?.let { return it }

            return synchronized(lock) {
                instance?.let { return@synchronized it }

                JobLauncherStore(errorHandler).also {
                    instance = it
                }
            }
        }
    }

    private val launchers =
        mutableMapOf<Triple<KClass<*>, KClass<*>, KClass<*>>, JobLauncher<*, *>>()

    @Suppress("UNCHECKED_CAST")
    fun <K : Any, T : Any> getOrCreateEnqueueingLauncher(
        keyClass: KClass<K>,
        targetClass: KClass<T>,
        dropQueueIfAnyFailed: Boolean = true
    ): JobLauncher<K, T> {
        return launchers.getOrPut(
            Triple(
                EnqueueingJobLauncher::class,
                keyClass,
                targetClass
            )
        ) {
            EnqueueingJobLauncher<K, T>(errorHandler, dropQueueIfAnyFailed)
        } as EnqueueingJobLauncher<K, T>
    }

    inline fun <reified K : Any, reified T : Any> getOrCreateEnqueueingLauncher(
        dropQueueIfAnyFailed: Boolean = true
    ) = getOrCreateEnqueueingLauncher(K::class, T::class, dropQueueIfAnyFailed)

    @Suppress("UNCHECKED_CAST")
    fun <K : Any, T : Any> getOrCreateCancellingLauncher(
        keyClass: KClass<K>,
        targetClass: KClass<T>,
    ): JobLauncher<K, T> {
        return launchers.getOrPut(
            Triple(
                CancellingJobLauncher::class,
                keyClass,
                targetClass
            )
        ) {
            CancellingJobLauncher<K, T>(errorHandler)
        } as CancellingJobLauncher<K, T>
    }

    inline fun <reified K : Any, reified T : Any> getOrCreateCancellingLauncher(): JobLauncher<K, T> {
        return getOrCreateCancellingLauncher(K::class, T::class)
    }
}
