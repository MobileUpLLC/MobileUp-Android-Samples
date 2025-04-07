package ru.mobileup.samples.core.map.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import ru.mobileup.samples.core.R

private val ACCURACY_CIRCLE_COLOR = Color.Transparent.toArgb()
const val DEFAULT_SCALE = 0.4f

class MyLocationMarkerOverlay : MapOverlay, UserLocationObjectListener {

    private var mapView: MapView? = null
    private var userLocationLayer: UserLocationLayer? = null
    private var mapKit: MapKit? = null

    override fun setup(mapView: MapView) {
        this.mapView = mapView
        mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit?.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer?.setObjectListener(this)
    }

    fun updateIsCurrentLocationAvailable(isCurrentLocationAvailable: Boolean) {
        mapKit?.let {
            if (isCurrentLocationAvailable) {
                it.resetLocationManagerToDefault()
                userLocationLayer?.isVisible = true
            } else {
                userLocationLayer?.isVisible = false
            }
        }
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        mapView?.let {
            val context = it.context

            userLocationView.accuracyCircle.fillColor = ACCURACY_CIRCLE_COLOR

            userLocationView.arrow.setIcon(
                ImageProvider.fromResource(context, R.drawable.ic_pin_my_location),
                IconStyle()
                    .setRotationType(RotationType.ROTATE)
                    .setScale(DEFAULT_SCALE)
            )

            userLocationView.pin.setIcon(
                ImageProvider.fromResource(context, R.drawable.ic_pin_my_location),
                IconStyle()
                    .setRotationType(RotationType.ROTATE)
                    .setScale(DEFAULT_SCALE)
            )
        }
    }

    override fun onObjectRemoved(userLocationView: UserLocationView) = Unit
    override fun onObjectUpdated(userLocationView: UserLocationView, event: ObjectEvent) = Unit
}