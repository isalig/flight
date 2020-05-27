package io.aiico.flight.presentation.flight

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import io.aiico.flight.domain.bezier.CubicCurveEvaluator
import io.aiico.flight.domain.interactor.FlightInteractor
import io.aiico.flight.domain.model.BezierCurveControlPoints
import io.aiico.flight.presentation.base.BasePresenter
import kotlin.math.abs

class FlightPresenter(
    private val flightInteractor: FlightInteractor,
    arguments: Bundle,
    view: FlightView
) : BasePresenter<FlightView>(view) {

    private var animator: ValueAnimator? = null
    private var flightProgress: Float = 0F

    private val startPointCoordinate: LatLng = arguments.getParcelable<LatLng>(KEY_START_COORDINATE) as LatLng
    private val endPointCoordinate: LatLng = arguments.getParcelable<LatLng>(KEY_END_COORDINATE) as LatLng
    private val startPointName: String = arguments.getString(KEY_START_POINT_NAME, "START")
    private val endPointName: String = arguments.getString(KEY_END_POINT_NAME, "END")

    private val controlPoints: BezierCurveControlPoints
    private val path: List<LatLng>
    private val evaluator: CubicCurveEvaluator

    private lateinit var planeCoordinate: LatLng
    private var planeRotation: Float = 0F

    init {
        val (controlPoints: BezierCurveControlPoints, path: List<LatLng>) =
            flightInteractor.calculateCubicPath(startPointCoordinate, endPointCoordinate)

        this.controlPoints = controlPoints
        this.path = path
        evaluator = CubicCurveEvaluator(controlPoints)
    }

    private val animatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            animator = null
        }
    }

    private val animatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        flightProgress = animation.animatedValue as Float

        val evaluatedPoint = evaluator.evaluate(flightProgress)
        val newPosition = flightInteractor.toLatLng(evaluatedPoint)

        // ignore "unreal" rotations
        val newRotation = calcPlaneRotation(newPosition)
        if (abs(newRotation - planeRotation) < MAX_PLANE_ROTATION_CHANGE) {
            planeRotation = newRotation

        }
        planeCoordinate = newPosition

        view.movePlaneMarker(planeCoordinate, planeRotation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        planeCoordinate = savedInstanceState
            ?.getParcelable(KEY_PLANE_COORDINATE)
            ?: startPointCoordinate

        planeRotation = savedInstanceState
            ?.getFloat(KEY_PLANE_ROTATION)
            ?: calcInitialPlaneRotation()

        savedInstanceState
            ?.getFloat(KEY_FLIGHT_PROGRESS)
            ?.let { savedProgress -> flightProgress = savedProgress }
    }

    private fun calcInitialPlaneRotation(): Float = calcPlaneRotation(flightInteractor.toLatLng(controlPoints.second))

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

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
        animator = null
    }

    fun onMapInitialization() {
        with(view) {
            showPointMarker(startPointCoordinate, startPointName)
            showPointMarker(endPointCoordinate, endPointName)
            showRoute(path)
            showPlaneMarker(planeCoordinate, planeRotation)
        }
    }

    private fun calcPlaneRotation(direction: LatLng): Float {
        val markerHeading = SphericalUtil
            .computeHeading(planeCoordinate, direction)
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
        with(ValueAnimator.ofFloat(flightProgress, MAX_FRACTION)) {
            interpolator = LinearInterpolator()
            duration = ((MAX_FRACTION - flightProgress) * ANIMATION_DURATION).toLong()
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

        private const val MAX_FRACTION = 1F
        private const val PLANE_ICON_DEFAULT_ROTATION = -90F
        private const val ANIMATION_DURATION = 10000
        private const val MAX_PLANE_ROTATION_CHANGE = 30
    }
}
