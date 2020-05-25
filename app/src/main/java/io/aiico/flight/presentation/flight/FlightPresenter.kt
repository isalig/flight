package io.aiico.flight.presentation.flight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import io.aiico.flight.presentation.base.BasePresenter
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class FlightPresenter(arguments: Bundle, view: FlightView) : BasePresenter<FlightView>(view) {

    private var animator: ValueAnimator? = null
    private var flightProgress: Float = 0F

    private val startPointCoordinate: LatLng = arguments.getParcelable<LatLng>(KEY_START_COORDINATE) as LatLng
    private val endPointCoordinate: LatLng = arguments.getParcelable<LatLng>(KEY_END_COORDINATE) as LatLng
    private val startPointName: String = arguments.getString(KEY_START_POINT_NAME, "START")
    private val endPointName: String = arguments.getString(KEY_END_POINT_NAME, "END")

    private lateinit var planeCoordinate: LatLng
    private var planeRotation: Float = 0F

    private val animatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            animator = null
        }
    }

    private val animatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        flightProgress = animation.animatedValue as Float
        planeCoordinate = SphericalUtil.interpolate(
            startPointCoordinate,
            endPointCoordinate,
            flightProgress.toDouble()
        )

        // ignore "unreal" rotations
        val newRotation = calcPlaneRotation()
        if (abs(newRotation - planeRotation) < MAX_PLANE_ROTATION_CHANGE) {
            planeRotation = newRotation

        }

        view.movePlaneMarker(planeCoordinate, planeRotation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        planeCoordinate = savedInstanceState
            ?.getParcelable(KEY_PLANE_COORDINATE)
            ?: startPointCoordinate

        planeRotation = savedInstanceState
            ?.getFloat(KEY_PLANE_ROTATION)
            ?: calcPlaneRotation()

        savedInstanceState
            ?.getFloat(KEY_FLIGHT_PROGRESS)
            ?.let { savedProgress -> flightProgress = savedProgress }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(outState) {
            putParcelable(KEY_PLANE_COORDINATE, planeCoordinate)
            putFloat(KEY_PLANE_ROTATION, planeRotation)
            putFloat(KEY_FLIGHT_PROGRESS, flightProgress)
        }
    }

    fun onViewBecomesVisible() {
        if (animator?.isPaused == true) {
            animator?.resume()
        }
    }

    fun onViewBecomesNotVisible() {
        if (animator?.isStarted == true) {
            animator?.pause()
        }
    }

    fun onMapInitialization() {
        with(view) {
            showPointMarker(startPointCoordinate, startPointName)
            showPointMarker(endPointCoordinate, endPointName)
            showRoute(startPointCoordinate, endPointCoordinate)
            showPlaneMarker(planeCoordinate, planeRotation)
        }
    }

    private fun calcPlaneRotation(): Float {
        val markerHeading = SphericalUtil
            .computeHeading(planeCoordinate, endPointCoordinate)
            .toFloat()
        return markerHeading + PLANE_ICON_DEFAULT_ROTATION
    }

    fun onMapLoaded() {
        view.showScene(startPointCoordinate, endPointCoordinate)
        if (flightProgress < 1F) {
            animateFlight()
        }
    }

    private fun animateFlight() {
        with(ValueAnimator.ofFloat(flightProgress, 1F)) {
            interpolator = AccelerateDecelerateInterpolator()
            duration = TimeUnit.SECONDS.toMillis(10)
            animator = this
            addUpdateListener(animatorUpdateListener)
            addListener(animatorListener)
            start()
        }
    }

    companion object {

        const val KEY_START_COORDINATE = "key_start_coordinate"
        const val KEY_START_POINT_NAME = "key_start_point_name"
        const val KEY_END_COORDINATE = "key_end_coordinate"
        const val KEY_END_POINT_NAME = "key_end_point_name"

        private const val KEY_PLANE_COORDINATE = "key_plane_coordinate"
        private const val KEY_PLANE_ROTATION = "key_plane_rotation"
        private const val KEY_FLIGHT_PROGRESS = "key_animation_progress"

        private const val MAX_PLANE_ROTATION_CHANGE = 30
        private const val PLANE_ICON_DEFAULT_ROTATION = -90F
    }
}
