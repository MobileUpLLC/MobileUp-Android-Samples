package ru.mobileup.samples.features.video

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.get
import org.koin.dsl.module
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.features.video.data.VideoFileManager
import ru.mobileup.samples.features.video.data.VideoFileManagerImpl
import ru.mobileup.samples.features.video.data.render.transformer.VideoRender
import ru.mobileup.samples.features.video.data.render.transformer.VideoRenderImpl
import ru.mobileup.samples.features.video.data.VideoRepository
import ru.mobileup.samples.features.video.data.VideoRepositoryImpl
import ru.mobileup.samples.features.video.presentation.RealVideoComponent
import ru.mobileup.samples.features.video.presentation.VideoComponent
import ru.mobileup.samples.features.video.presentation.menu.RealVideoMenuComponent
import ru.mobileup.samples.features.video.presentation.menu.VideoMenuComponent
import ru.mobileup.samples.features.video.presentation.player.RealVideoPlayerComponent
import ru.mobileup.samples.features.video.presentation.player.VideoPlayerComponent
import ru.mobileup.samples.features.video.presentation.recorder.RealVideoRecorderComponent
import ru.mobileup.samples.features.video.presentation.recorder.VideoRecorderComponent

val videoModule = module {
    single<VideoRepository> { VideoRepositoryImpl(get()) }
    single<VideoFileManager> { VideoFileManagerImpl(get()) }
    single<VideoRender> { VideoRenderImpl(get()) }
}

fun ComponentFactory.createVideoComponent(componentContext: ComponentContext): VideoComponent {
    return RealVideoComponent(componentContext, get(), get())
}

fun ComponentFactory.createVideoMenuComponent(
    componentContext: ComponentContext,
    onOutput: (VideoMenuComponent.Output) -> Unit
): VideoMenuComponent {
    return RealVideoMenuComponent(componentContext, get(), onOutput)
}

fun ComponentFactory.createVideoRecorderComponent(
    componentContext: ComponentContext,
    onOutput: (VideoRecorderComponent.Output) -> Unit
): VideoRecorderComponent {
    return RealVideoRecorderComponent(componentContext, get(), onOutput)
}

fun ComponentFactory.createVideoPlayerComponent(
    uri: Uri,
    componentContext: ComponentContext,
): VideoPlayerComponent {
    return RealVideoPlayerComponent(uri, componentContext, get(), get(), get(), get())
}