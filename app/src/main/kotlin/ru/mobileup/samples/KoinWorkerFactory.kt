package ru.mobileup.samples

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

internal class KoinWorkerFactory(private val koin: Koin) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? = koin.getOrNull(qualifier = named(workerClassName)) {
        parametersOf(workerParameters)
    }
}
