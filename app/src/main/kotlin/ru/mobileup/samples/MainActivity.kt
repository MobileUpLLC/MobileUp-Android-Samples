package ru.mobileup.samples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.essenty.lifecycle.asEssentyLifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.glide.GlideDivImageLoader
import com.yandex.div.video.ExoDivPlayerFactory
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.koin
import ru.mobileup.samples.core.theme.AppTheme
import ru.mobileup.samples.features.root.createRootComponent
import ru.mobileup.samples.features.root.presentation.RootUi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val activityProvider = application.koin.get<ActivityProvider>()
        activityProvider.attachActivity(this)
        lifecycle.asEssentyLifecycle().doOnDestroy {
            activityProvider.detachActivity()
        }

        prepareYandexDivKit()

        val rootComponent = retainedComponent { componentContext ->
            val componentFactory = application.koin.get<ComponentFactory>()
            componentFactory.createRootComponent(componentContext)
        }

        setContent {
            val theme by rootComponent.themeComponent.theme.collectAsState()

            AppTheme(theme) {
                RootUi(rootComponent)
            }
        }
    }

    private fun prepareYandexDivKit() {
        /**
         * Так как инициализация ExoPlayer тяжелая операция, плеер подключается только при явном
         * указании его необходимости в классе DivConfiguration.
         * Если вы не планируете использовать видеоплеер в своем приложении,
         * не вызывайте метод .divPlayerFactory в билдере (строителе) класса DivConfiguration.
         */
        val divImageLoader = GlideDivImageLoader(this)
        val divConfig = DivConfiguration
            .Builder(divImageLoader)
            .divPlayerFactory(ExoDivPlayerFactory(this))
            .build()
        val divContext = Div2Context(
            baseContext = this,
            configuration = divConfig,
            lifecycleOwner = this
        )
        application.koin.declare(divContext)
    }
}
