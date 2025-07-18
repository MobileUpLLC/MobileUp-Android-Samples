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
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

class AndroidLocationService(
    private val context: Context
) : LocationService {

    override suspend fun getCurrentLocation(timeout: Duration): GeoCoordinate {
        return try {
            val fusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(context)
            getLocation(fusedLocationProviderClient, timeout)
        } catch (e: CancellationException) {
            throw e
        } catch (e: LocationNotAvailableException) {
            throw e
        } catch (e: Exception) {
            throw LocationNotAvailableException(e, message = null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    private suspend fun getLocation(
        client: FusedLocationProviderClient,
        timeout: Duration
    ): GeoCoordinate {
        val cancellationTokenSource = CancellationTokenSource()
        return try {
            withTimeout(timeout) {
                client
                    .getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                    .await(cancellationTokenSource)
                    .let { location ->
                        GeoCoordinate(location.latitude, location.longitude)
                    }
            }
        } catch (e: CancellationException) {
            cancellationTokenSource.cancel()
            throw e
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
            GeoCoordinate(location.latitude, location.longitude)
        } ?: throw LocationNotAvailableException(cause = null, "Last location is null")
    }
}
