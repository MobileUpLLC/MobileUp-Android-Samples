package ru.mobileup.samples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.koin
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.features.root.createRootComponent
import ru.mobileup.samples.features.root.presentation.RootUi

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val activityProvider = application.koin.get<ActivityProvider>()
        activityProvider.attachActivity(this)
        lifecycle.asEssentyLifecycle().doOnDestroy {
            activityProvider.detachActivity()
        }

        val rootComponent = retainedComponent { componentContext ->
            val componentFactory = application.koin.get<ComponentFactory>()
            componentFactory.createRootComponent(componentContext)
        }

        setContent {
            AppTheme {
                RootUi(rootComponent)
            }
        }
    }
}
