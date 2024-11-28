package com.vazh2100.core.domain.entities

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GPoint(val lat: Double, val lon: Double) {

    /**
     * Calculates the geographical distance between the point and other point using the Haversine formula.
     * @return The distance in kilometers.
     */
    fun distanceTo(other: GPoint): Double {
        val r = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(other.lat - this.lat)
        val dLon = Math.toRadians(other.lon - this.lon)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(this.lat)) * cos(Math.toRadians(other.lat)) * sin(
                dLon / 2
            ) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}