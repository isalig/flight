package io.aiico.flight.domain.interactor

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.geometry.Point
import com.google.maps.android.projection.SphericalMercatorProjection
import io.aiico.flight.domain.bezier.pointInCubicCurve
import io.aiico.flight.domain.model.BezierCurveControlPoints
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FlightInteractor {

    fun calculateCubicPath(
        startPosition: LatLng,
        endPosition: LatLng
    ): Pair<BezierCurveControlPoints, List<LatLng>> {
        val startPoint = projection.toPoint(startPosition).run { Point(x, y) }
        val endPoint = projection.toPoint(endPosition).run { Point(x, y) }

        val dx = startPoint.x - endPoint.x
        val dy = startPoint.y - endPoint.y

        val (secondControlPoint, thirdControlPoint) = getIntermediateControlPoints(startPoint, dx, dy)
        val controlPoints = BezierCurveControlPoints(startPoint, secondControlPoint, thirdControlPoint, endPoint)

        val curve = mutableListOf<LatLng>()
        var fraction = 0.0

        while (fraction < 1) {
            val point = pointInCubicCurve(startPoint, secondControlPoint, thirdControlPoint, endPoint, fraction)
            curve.add(projection.toLatLng(point))
            fraction += 0.01
        }
        curve.add(projection.toLatLng(endPoint))

        return controlPoints to curve
    }

    private fun getIntermediateControlPoints(startPoint: Point, dx: Double, dy: Double): Pair<Point, Point> {

        val dxHalf = dx * 0.5
        val dyHalf = dy * 0.5

        val midPoint = Point(startPoint.x - dxHalf, startPoint.y - dyHalf)
        val r = sqrt(dxHalf * dxHalf + dyHalf * dyHalf)

        // angle between start point and end point
        var angle = Math.toDegrees(atan2(-dy, -dx))
        angle = Math.toRadians(CONTROL_POINT_ANGLE_OFFSET - angle)

        // coordinates of control point from (0, 0)
        val x = r * cos(angle)
        val y = r * sin(angle)

        return Point(midPoint.x + x, midPoint.y - y) to Point(midPoint.x - x, midPoint.y + y)
    }

    fun toLatLng(point: Point): LatLng = projection.toLatLng(point)

    companion object {

        private const val WORLD_WIDTH: Double = 1.0
        private const val CONTROL_POINT_ANGLE_OFFSET: Double = 90.0
        private val projection: SphericalMercatorProjection = SphericalMercatorProjection(WORLD_WIDTH)
    }
}
