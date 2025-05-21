package ru.mobileup.samples

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.yandex.mapkit.MapKitFactory
import org.koin.core.Koin
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.KoinProvider
import ru.mobileup.samples.core.debug_tools.DebugTools
import ru.mobileup.samples.core.BuildConfig as CoreBuildConfig

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        initLogger()
        koin = createKoin()
        launchDebugTools()
        initWorkManagerWithKoinFactory()
        MapKitFactory.setApiKey(CoreBuildConfig.YANDEX_MAP_API_KEY)
    }

    private fun initLogger() {
        if (!BuildConfig.DEBUG) {
            Logger.setMinSeverity(Severity.Assert)
        }
    }

    private fun createKoin(): Koin {
        return Koin().apply {
            loadModules(allModules)
            declare(this@App as Application)
            declare(this@App as Context)
            declare(ComponentFactory(this))
            createEagerInstances()
        }
    }

    private fun launchDebugTools() {
        koin.get<DebugTools>().launch()
    }

    private fun initWorkManagerWithKoinFactory() {
        val delegatingFactory = DelegatingWorkerFactory().apply {
            addFactory(KoinWorkerFactory(koin))
        }

        val config = Configuration.Builder()
            .setWorkerFactory(delegatingFactory)
            .build()

        WorkManager.initialize(this, config)

        koin.getAll<WorkerFactory>().forEach(delegatingFactory::addFactory)
    }
}
