package ru.mobileup.samples.core.location

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.Serializable

const val DEFAULT_MAP_ZOOM = 6f
const val CLOSE_MAP_ZOOM = 16f

@Serializable
data class GeoCoordinate(
    val lat: Double,
    val lng: Double,
) {
    companion object {
        val KREMLIN = GeoCoordinate(55.752004, 37.617734)

        fun getPlacesInMoscowRegion(list: List<GeoCoordinate>): List<GeoCoordinate> =
            list.filter { place ->
                place.lat in (KREMLIN.lat - 1..KREMLIN.lat + 1) &&
                    place.lng in (KREMLIN.lng - 1..KREMLIN.lng + 1)
            }

        private val MOSCOW = listOf(
            GeoCoordinate(55.741004, 37.587734),
            GeoCoordinate(55.772004, 37.617734),
            GeoCoordinate(55.723004, 37.557734),
            GeoCoordinate(55.734004, 37.617734),
            GeoCoordinate(55.745004, 37.607734),
        )

        private val PERM = listOf(
            GeoCoordinate(58.012762, 56.232483),
            GeoCoordinate(58.010762, 56.234483),
            GeoCoordinate(58.014762, 56.232483),
            GeoCoordinate(58.010762, 56.235483),
            GeoCoordinate(58.019762, 56.232483),
        )

        private val SPB = listOf(
            GeoCoordinate(59.933887, 30.324289),
            GeoCoordinate(59.934887, 30.324289),
            GeoCoordinate(59.933887, 30.325289),
            GeoCoordinate(59.935887, 30.324289),
            GeoCoordinate(59.933887, 30.326289),
            GeoCoordinate(59.936887, 30.324289),
        )

        val MOCKS = listOf(MOSCOW, PERM, SPB).flatten()
    }
}

@Serializable
data class GeoCameraPosition(
    val geoCoordinate: GeoCoordinate,
    val zoom: Float,
    val azimuth: Float,
    val tilt: Float
)

val GeoCameraPositionSaver: Saver<GeoCameraPosition, *> = listSaver(
    save = { position ->
        listOf(
            position.geoCoordinate.lat,
            position.geoCoordinate.lng,
            position.zoom,
            position.azimuth,
            position.tilt
        )
    },
    restore = { values ->
        GeoCameraPosition(
            geoCoordinate = GeoCoordinate(
                lat = values.getSafe(0, GeoCoordinate.KREMLIN.lat),
                lng = values.getSafe(1, GeoCoordinate.KREMLIN.lng)
            ),
            zoom = values.getSafe(2, DEFAULT_MAP_ZOOM),
            azimuth = 0f,
            tilt = 0f
        )
    }
)

inline fun <reified T> List<*>.getSafe(index: Int, default: T): T {
    return try {
        this.getOrNull(index) as? T ?: default
    } catch (_: ClassCastException) {
        default
    }
}
