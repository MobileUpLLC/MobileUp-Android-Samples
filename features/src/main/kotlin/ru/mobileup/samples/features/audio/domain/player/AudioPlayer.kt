package ru.mobileup.samples.features.audio.domain.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mobileup.samples.features.audio.domain.model.AudioFile

private const val POSITION_REFRESH_DELAY_MILLIS = 18L

class AudioPlayer(
    private val context: Context,
) : Player.Listener {

    private val progressScope = CoroutineScope(Dispatchers.Main)

    private val factory: DefaultMediaSourceFactory = DefaultMediaSourceFactory(context)

    private var progressJob: Job? = null

    private val _playerState = MutableStateFlow<AudioPlayerState>(AudioPlayerState.Idle)

    val playerState = _playerState.asStateFlow()

    private var isBuffering = false

    private val _progressState = MutableStateFlow(0L)

    val progressState = _progressState.asStateFlow()

    private var player: ExoPlayer = createExoPlayer()

    fun play(
        file: AudioFile,
        playedDurationMs: Long = 0
    ) {
        player.configure(
            uri = file.uri,
            playedDurationMs = playedDurationMs
        )
        _progressState.value = playedDurationMs
        player.play()
    }

    fun stop() {
        player.stop()
    }

    fun release() {
        player.release()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        // Don't listen position during buffering
        isBuffering = playbackState == Player.STATE_BUFFERING
        if (playbackState == Player.STATE_ENDED) {
            cancelAudioProgressUpdate()
            _progressState.value = 0L
        }
        super.onPlaybackStateChanged(playbackState)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playerState.value = if (isPlaying) AudioPlayerState.Playing else AudioPlayerState.Idle
        if (isPlaying) {
            startAudioProgressUpdate()
        }
    }

    @OptIn(UnstableApi::class)
    private fun ExoPlayer.configure(
        uri: Uri,
        playedDurationMs: Long = 0
    ) {
        repeatMode = Player.REPEAT_MODE_OFF
        this.playWhenReady = playWhenReady
        clearMediaItems()

        val mediaSource = factory.createMediaSource(
            MediaItem.Builder()
                .setUri(uri)
                .build()
        )
        setMediaSource(mediaSource)
        prepare()
        seekTo(playedDurationMs)
    }

    @OptIn(UnstableApi::class)
    private fun createExoPlayer(): ExoPlayer {
        return ExoPlayer.Builder(context, factory)
            .setSeekParameters(SeekParameters.EXACT)
            .build()
            .apply {
                playWhenReady = false
                addListener(this@AudioPlayer)
                prepare()
            }
    }

    private fun startAudioProgressUpdate() {
        cancelAudioProgressUpdate()
        progressJob = progressScope.launch {
            audioProgressUpdate()
        }
    }

    private fun cancelAudioProgressUpdate() {
        progressJob?.cancel()
        progressJob = null
    }

    private suspend fun audioProgressUpdate() {
        player.run {
            if (!isBuffering && isPlaying) {
                delay(POSITION_REFRESH_DELAY_MILLIS)
                _progressState.emit(player.currentPosition)
                audioProgressUpdate()
            }
        }
    }
}