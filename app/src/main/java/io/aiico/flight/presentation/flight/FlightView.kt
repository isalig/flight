package io.aiico.flight.presentation.flight

import com.google.android.gms.maps.model.LatLng
import io.aiico.flight.presentation.base.BaseView

interface FlightView : BaseView {

    fun showPointMarker(position: LatLng, title: String)

    fun showRoute(path: List<LatLng>)

    fun showScene(vararg boundsPoints: LatLng)

    fun showPlaneMarker(position: LatLng, rotation: Float)

    fun movePlaneMarker(position: LatLng, rotation: Float)
}
