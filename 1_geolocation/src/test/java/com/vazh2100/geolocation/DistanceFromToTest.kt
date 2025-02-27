package com.vazh2100.geolocation

import com.vazh2100.geolocation.entity.GPoint
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DistanceFromToTest {

    @Test
    fun `when points the same, a distance is 0`() {
        val point1 = GPoint(52.5200, 13.4050) // Berlin
        val point2 = GPoint(52.5200, 13.4050) // Berlin
        val distance = point1.distanceTo(point2)
        assertEquals(0.0, distance, 0.0) // The distance should be 0, with no margin of error.
    }

    @Test
    fun `when points are well known cities, a distance is expected`() {
        val moscow = GPoint(55.7558, 37.6173) // Moscow
        val saintPetersburg = GPoint(59.9343, 30.3351) // Saint Petersburg
        val expected = 633.0
        val distance = moscow.distanceTo(saintPetersburg)
        assertEquals(expected, distance, 1.0) // Allow a margin of error of 1 km.
    }

    /**
     * Test case for calculating the distance between two random cities (Paris and Los Angeles).
     * The expected distance is approximately 9085.5 km.
     */
    @Test
    fun `when points are well known cities 2, a distance is expected`() {
        val point1 = GPoint(48.8566, 2.3522) // Paris
        val point2 = GPoint(34.0522, -118.2437) // Los Angeles
        val expected = 9085.5
        val distance = point1.distanceTo(point2)
        // Expected distance: approximately 9085.5 km
        assertEquals(expected, distance, 1.0) // Allow a margin of error of 50 km.
    }

    @Test
    fun `when points belongs to the equator, a distance is expected`() {
        val point1 = GPoint(0.0, 0.0) // Point on the equator and the Prime Meridian
        val point2 = GPoint(0.0, 10.0) // Point on the equator, but 10° east
        val expected = 1112.0
        val distance = point1.distanceTo(point2)
        assertEquals(expected, distance, 1.0) // Allow a margin of error of 1 km.
    }
}
