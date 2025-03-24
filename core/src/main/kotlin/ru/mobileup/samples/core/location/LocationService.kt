package ru.mobileup.samples.core.location

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface LocationService {
    suspend fun getCurrentLocation(timeout: Duration = 10.seconds): GeoCoordinate
}