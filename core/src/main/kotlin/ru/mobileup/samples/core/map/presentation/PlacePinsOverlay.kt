package ru.mobileup.samples.core.map.presentation

import android.view.LayoutInflater
import android.widget.TextView
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider
import ru.mobileup.samples.core.R
import ru.mobileup.samples.core.location.GeoCoordinate
import ru.mobileup.samples.core.map.utils.toPoint
import kotlin.collections.forEach

private const val CLUSTER_RADIUS = 40.0
private const val CLUSTER_MIN_ZOOM = 12
private const val PLACE_PIN_SCALE = 0.2f

class PlacePinsOverlay(
    onPlacePinClick: (GeoCoordinate) -> Unit
) : MapOverlay {

    private var mapView: MapView? = null
    private var clusterListener: ClusterListener? = null
    private var clusterCollection: ClusterizedPlacemarkCollection? = null

    private val markerTapListener = MapObjectTapListener { mapObject, _ ->
        val place = mapObject.userData as? GeoCoordinate ?: return@MapObjectTapListener false
        onPlacePinClick(place)
        true
    }

    override fun setup(mapView: MapView) {
        this.mapView = mapView
        clusterListener = ClusterListener { cluster ->
            cluster.appearance.setView(
                ViewProvider(
                    LayoutInflater.from(mapView.context).inflate(
                        R.layout.cluster_view, null, false
                    ).apply {
                        val clusterSize = findViewById<TextView>(R.id.clusterSizeTv)
                        clusterSize.text = cluster.size.toString()
                    }

                )
            )
        }
    }

    fun updatePlaces(places: List<GeoCoordinate>) {
        val map = mapView?.mapWindow?.map ?: return
        map.mapObjects.clear()
        clusterCollection = map.mapObjects.addClusterizedPlacemarkCollection(clusterListener!!)
        places.forEach { place: GeoCoordinate ->
            addPlacePin(place)
        }
        clusterCollection?.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
        clusterCollection?.isVisible = true
    }

    private fun addPlacePin(place: GeoCoordinate) {
        val context = mapView?.context
        clusterCollection?.addPlacemark()?.apply {
            userData = place
            geometry = place.toPoint()
            addTapListener(markerTapListener)
            setIcon(
                ImageProvider.fromResource(
                    context,
                    R.drawable.ic_map_pin,
                    true
                ),
                IconStyle().apply { scale = PLACE_PIN_SCALE }
            )
        }
    }
}