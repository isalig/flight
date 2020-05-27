package io.aiico.flight.domain.bezier

import com.google.maps.android.geometry.Point

fun lerp(x: Double, y: Double, t: Double): Double {
    return x * (1 - t) + y * t
}

fun pointInLine(point1: Point, point2: Point, fraction: Double): Point {
    return Point(lerp(point1.x, point2.x, fraction), lerp(point1.y, point2.y, fraction))
}

fun pointInQuadCurve(
    point1: Point,
    point2: Point,
    point3: Point,
    fraction: Double
): Point {
    val pointA = pointInLine(point1, point2, fraction)
    val pointB = pointInLine(point2, point3, fraction)
    return pointInLine(pointA, pointB, fraction)
}

fun pointInCubicCurve(
    point1: Point,
    point2: Point,
    point3: Point,
    point4: Point,
    fraction: Double
): Point {
    val pointA = pointInQuadCurve(point1, point2, point3, fraction)
    val pointB = pointInQuadCurve(point2, point3, point4, fraction)
    return pointInLine(pointA, pointB, fraction)
}
