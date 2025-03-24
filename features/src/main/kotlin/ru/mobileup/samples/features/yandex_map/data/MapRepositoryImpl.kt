package ru.mobileup.samples.features.yandex_map.data

import kotlinx.coroutines.delay
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.single.Replica
import me.aartikov.replica.single.ReplicaSettings
import ru.mobileup.samples.core.location.GeoCoordinate
import kotlin.time.Duration.Companion.hours

class MapRepositoryImpl(
    replicaClient: ReplicaClient
) : MapRepository {
    override val placesReplica: Replica<List<GeoCoordinate>> = replicaClient.createReplica(
        name = "places",
        settings = ReplicaSettings(staleTime = 1.hours),
        fetcher = {
            delay(2000)
            GeoCoordinate.MOCKS
        }
    )
}