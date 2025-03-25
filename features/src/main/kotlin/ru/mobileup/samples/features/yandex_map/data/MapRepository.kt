package ru.mobileup.samples.features.yandex_map.data

import me.aartikov.replica.single.Replica
import ru.mobileup.samples.core.location.GeoCoordinate

interface MapRepository {
    val placesReplica: Replica<List<GeoCoordinate>>
}