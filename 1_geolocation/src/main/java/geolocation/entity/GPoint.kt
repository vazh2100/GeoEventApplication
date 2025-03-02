package geolocation.entity

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GPoint(val lat: Double, val lon: Double) {

    private companion object {
        const val EARTH_RADIUS = 6371.0 // km
    }

    /**
     * Distance between the points using the Haversine formula.
     */
    fun distanceTo(other: GPoint): Double {
        val dLat = Math.toRadians(other.lat - this.lat)
        val dLon = Math.toRadians(other.lon - this.lon)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(this.lat)) * cos(Math.toRadians(other.lat)) * sin(
                dLon / 2
            ) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }
}
