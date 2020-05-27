package io.aiico.flight.domain.model

import com.google.maps.android.geometry.Point

data class BezierCurveControlPoints(
    val first: Point,
    val second: Point,
    val third: Point,
    val fourth: Point
)
