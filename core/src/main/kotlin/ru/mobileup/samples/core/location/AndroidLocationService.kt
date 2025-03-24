package ru.mobileup.samples.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration

class AndroidLocationService(
    private val context: Context
) : LocationService {

    override suspend fun getCurrentLocation(timeout: Duration): GeoCoordinate {
        return withTimeout(timeout) {
            val fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context)

            getLocation(
                client = fusedLocationProviderClient,
                timeout = timeout
            )
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
        return client.lastLocation.await()
            .let { location ->
                GeoCoordinate(
                    lat = location.latitude,
                    lng = location.longitude
                )
            }
    }
}
