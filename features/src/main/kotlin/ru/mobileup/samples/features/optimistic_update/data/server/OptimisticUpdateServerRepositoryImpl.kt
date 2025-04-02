package ru.mobileup.samples.features.optimistic_update.data.server

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import me.aartikov.replica.algebra.normal.flowReplica
import ru.mobileup.samples.core.error_handling.ServerException
import ru.mobileup.samples.features.optimistic_update.data.OptimisticUpdateApi
import ru.mobileup.samples.features.optimistic_update.domain.PaletteColor
import ru.mobileup.samples.features.optimistic_update.domain.PaletteRequest

private typealias Request = Pair<CompletableDeferred<Boolean>, PaletteRequest>

class OptimisticUpdateServerRepositoryImpl : OptimisticUpdateServerRepository, OptimisticUpdateApi {

    private val deferredRequests = MutableStateFlow<List<Request>>(listOf())
    private val palette = MutableStateFlow<List<PaletteColor>>(listOf())

    override val paletteReplica = flowReplica(palette)

    override val requestsReplica = flowReplica(
        deferredRequests.map { list ->
            list.map { it.second }
        }
    )

    override fun acceptRequestAt(index: Int) {
        val (deferred, request) = deferredRequests.value.getOrNull(index) ?: return
        if (!request.canBeAccepted) return

        val changedRequest = when (request) {
            is PaletteRequest.AddRequest -> {
                palette.value += request.color
                request.copy(canBeAccepted = false)
            }
            is PaletteRequest.RemoveRequest -> {
                palette.update { list ->
                    list.filter {
                        it != request.color
                    }
                }
                request.copy(canBeAccepted = false)
            }
        }

        deferredRequests.update {
            it.toMutableList().apply {
                set(index, deferred to changedRequest)
            }
        }
    }

    override fun failRequestAt(index: Int) {
        val deferred = deferredRequests.value.getOrNull(index)?.first ?: return
        deferred.complete(false)
    }

    override fun endRequestAt(index: Int) {
        val deferred = deferredRequests.value.getOrNull(index)?.first ?: return
        deferred.complete(true)
    }

    // Api

    override suspend fun getPaletteSize() = palette.value.size

    override suspend fun addToPalette(color: Long) {
        val paletteColor = PaletteColor(color)

        awaitRequest(
            PaletteRequest.AddRequest(
                color = paletteColor,
                canBeAccepted = !palette.value.contains(paletteColor)
            )
        )
    }

    override suspend fun removeFromPalette(color: Long) {
        val paletteColor = PaletteColor(color)

        awaitRequest(
            PaletteRequest.RemoveRequest(
                color = PaletteColor(color),
                canBeAccepted = palette.value.contains(paletteColor)
            )
        )
    }

    private suspend fun awaitRequest(request: PaletteRequest) {
        val deferred = CompletableDeferred<Boolean>()

        try {
            deferredRequests.value += deferred to request
            if (!deferred.await()) {
                throw ServerException(null)
            }
        } finally {
            deferredRequests.update { set ->
                set.filter { it.first !== deferred }
            }
        }
    }

    override suspend fun getPalette(): List<Long> = palette.value.map(PaletteColor::value)

    override suspend fun getAvailableColors(): List<Long> {
        return listOf(
            Color.Black,
            Color.DarkGray,
            Color.Gray,
            Color.LightGray,
            Color.White,
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
        ).map(Color::toArgb).map(Int::toLong)
    }
}