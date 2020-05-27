package io.aiico.flight.presentation.flight

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ui.IconGenerator
import io.aiico.flight.R
import io.aiico.flight.ServiceLocator
import io.aiico.flight.domain.model.Destination
import io.aiico.flight.presentation.base.BaseFragment
import io.aiico.flight.presentation.flight.FlightPresenter.Companion.KEY_END_COORDINATE
import io.aiico.flight.presentation.flight.FlightPresenter.Companion.KEY_END_POINT_NAME
import io.aiico.flight.presentation.flight.FlightPresenter.Companion.KEY_START_COORDINATE
import io.aiico.flight.presentation.flight.FlightPresenter.Companion.KEY_START_POINT_NAME

class FlightFragment :
    BaseFragment<FlightPresenter>(),
    FlightView,
    OnMapReadyCallback,
    OnMapLoadedCallback {

    private lateinit var map: GoogleMap
    private lateinit var planeMarker: Marker

    private var dotSize: Float = 0F
    private var cameraPadding: Int = 0

    override fun createPresenter(): FlightPresenter =
        FlightPresenter(ServiceLocator.getFlightInteractor(), requireArguments(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dotSize = resources.getDimension(R.dimen.route_dot_size)
        cameraPadding = resources.getDimensionPixelSize(R.dimen.map_camera_padding)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_flight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment).getMapAsync(
            this
        )
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewBecomesVisible()
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewBecomesNotVisible()
    }

    override fun onMapReady(preparedMap: GoogleMap) {
        map = preparedMap
        initMap()
    }

    private fun initMap() {
        map.setOnMapLoadedCallback(this)

        with(map.uiSettings) {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = false
            isMapToolbarEnabled = false
            isMyLocationButtonEnabled = false
        }

        presenter.onMapInitialization()
    }

    override fun showPointMarker(position: LatLng, title: String) {
        with(MarkerOptions()) {
            position(position)
            icon(BitmapDescriptorFactory.fromBitmap(generateCityLabel(title)))
            flat(true)
            map.addMarker(this)
        }
    }

    private fun generateCityLabel(title: String): Bitmap {
        return with(IconGenerator(requireContext())) {
            setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.background_marker))
            setTextAppearance(R.style.TextAppearance_Flight_MarkerIcon)
            makeIcon(title)
        }
    }

    override fun showRoute(path: List<LatLng>) {
        with(PolylineOptions()) {
            path.forEach { point ->
                add(point)
            }
            width(dotSize)
            geodesic(true)
            color(ContextCompat.getColor(requireContext(), R.color.colorRoute))
            pattern(mutableListOf(Dot(), Gap(dotSize)))
            map.addPolyline(this)
        }
    }

    override fun showPlaneMarker(position: LatLng, rotation: Float) {
        with(MarkerOptions()) {
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
            position(position)
            rotation(rotation)
            anchor(0.5F, 0.5F)
            zIndex(1F)
            flat(true)
            planeMarker = map.addMarker(this)
        }
    }

    override fun movePlaneMarker(position: LatLng, rotation: Float) {
        planeMarker.position = position
        planeMarker.rotation = rotation
    }

    override fun onMapLoaded() {
        presenter.onMapLoaded()
    }

    override fun showScene(vararg boundsPoints: LatLng) {
        with(LatLngBounds.Builder()) {
            boundsPoints.forEach { point ->
                include(point)
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(build(), cameraPadding))
        }
    }

    companion object {

        fun newInstance(startDestination: Destination, endDestination: Destination) =
            FlightFragment().apply {
                arguments = bundleOf(
                    KEY_START_POINT_NAME to startDestination.iata,
                    KEY_END_POINT_NAME to endDestination.iata,
                    KEY_START_COORDINATE to LatLng(
                        startDestination.location.latitude.toDouble(),
                        startDestination.location.longitude.toDouble()
                    ),
                    KEY_END_COORDINATE to LatLng(
                        endDestination.location.latitude.toDouble(),
                        endDestination.location.longitude.toDouble()
                    )
                )
            }
    }
}
