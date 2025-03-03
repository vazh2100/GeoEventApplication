package geolocation

import geolocation.entity.GPoint
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DistanceFromToTest {
    private val cases = listOf(
        // The same
        Case(GPoint(52.5200, 13.4050), GPoint(52.5200, 13.4050), 0.0, 0.0),
        // Moscow - Saint Petersburg
        Case(GPoint(55.7558, 37.6173), GPoint(59.9343, 30.3351), 633.0, 1.0),
        // Paris - Los Angeles
        Case(GPoint(48.8566, 2.3522), GPoint(34.0522, -118.2437), 9085.5, 1.0),
        // Points on the equator
        Case(GPoint(0.0, 0.0), GPoint(0.0, 10.0), 1112.0, 1.0)
    )

    @Test
    @Suppress("DestructuringDeclarationWithTooManyEntries")
    fun `when points are well known, a distance is expected`() {
        for ((point1, point2, expected, delta) in cases) {
            assertEquals(expected, point1.distanceTo(point2), delta)
        }
    }

    data class Case(val point1: GPoint, val point2: GPoint, val expected: Double, val delta: Double)
}
