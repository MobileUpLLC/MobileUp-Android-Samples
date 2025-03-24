package ru.mobileup.samples

import android.app.Application
import android.content.Context
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.koin.core.Koin
import com.yandex.mapkit.MapKitFactory
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.KoinProvider
import ru.mobileup.samples.core.debug_tools.DebugTools

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        initLogger()
        koin = createKoin()
        launchDebugTools()
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAP_API_KEY)
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
}
