package io.aiico.flight.domain.bezier

import android.animation.TypeEvaluator
import com.google.maps.android.geometry.Point
import io.aiico.flight.domain.model.BezierCurveControlPoints

class CubicCurveEvaluator(private val controlPoints: BezierCurveControlPoints) :
    TypeEvaluator<Point> {

    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point = pointInCubicCurve(
        controlPoints.first,
        controlPoints.second,
        controlPoints.third,
        controlPoints.fourth,
        fraction.toDouble()
    )

    fun evaluate(fraction: Float): Point {
        return evaluate(fraction, controlPoints.first, controlPoints.fourth)
    }

}
