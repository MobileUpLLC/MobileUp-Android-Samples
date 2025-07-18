package ru.mobileup.samples.core.location

import ru.mobileup.samples.core.error_handling.LocationNotAvailableException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface LocationService {
    @Throws(LocationNotAvailableException::class)
    suspend fun getCurrentLocation(timeout: Duration = 10.seconds): GeoCoordinate
}