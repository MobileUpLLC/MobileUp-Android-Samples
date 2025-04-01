package ru.mobileup.samples.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import ru.mobileup.samples.core.error_handling.LocationNotAvailableException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

class AndroidLocationService(
    private val context: Context
) : LocationService {

    private companion object {
        const val DEFAULT_TIMEOUT_SECONDS = 10
    }

    override suspend fun getCurrentLocation(): GeoCoordinate {
        return try {
            val fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context)
            getLocation(
                client = fusedLocationProviderClient,
                timeout = DEFAULT_TIMEOUT_SECONDS.nanoseconds
            )
        } catch (e: Exception) {
            throw LocationNotAvailableException(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    private suspend fun getLocation(
        client: FusedLocationProviderClient,
        timeout: Duration
    ): GeoCoordinate {
        return try {
            withTimeout(timeout) {
                val cancellationTokenSource = CancellationTokenSource()
                client
                    .getCurrentLocation(
                        PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).await(cancellationTokenSource)
                    .let { location ->
                        GeoCoordinate(
                            lat = location.latitude,
                            lng = location.longitude
                        )
                    }
            }
        } catch (_: Exception) {
            getLastLocation(client)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(
        client: FusedLocationProviderClient
    ): GeoCoordinate {
        val location: Location? = client.lastLocation.await()
        return location?.let { location ->
            GeoCoordinate(
                lat = location.latitude,
                lng = location.longitude
            )
        } ?: throw LocationNotAvailableException(message = "Last location is null")
    }
}
