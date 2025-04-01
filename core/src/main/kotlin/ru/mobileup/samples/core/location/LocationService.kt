package ru.mobileup.samples.core.location

import ru.mobileup.samples.core.error_handling.LocationNotAvailableException

interface LocationService {
    @Throws(LocationNotAvailableException::class)
    suspend fun getCurrentLocation(): GeoCoordinate
}