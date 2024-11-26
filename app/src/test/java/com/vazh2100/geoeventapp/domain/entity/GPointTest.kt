package com.vazh2100.geoeventapp.domain.entity

import com.vazh2100.geoeventapp.domain.entities.GPoint
import org.junit.Assert.*
import org.junit.Test

class GPointTest {

    /**
     * Test case for calculating distance between the same point.
     * The distance between two identical points should always be 0.
     */
    @Test
    fun testDistanceTo_samePoint() {
        val point1 = GPoint(52.5200, 13.4050) // Berlin
        val point2 = GPoint(52.5200, 13.4050) // Berlin

        val distance = point1.distanceTo(point2)

        assertEquals(0.0, distance, 0.0) // The distance should be 0, with no margin of error.
    }

    /**
     * Test case for calculating the distance between two well-known cities (Moscow and Saint Petersburg).
     * The expected result is approximately 633.0 km.
     */
    @Test
    fun testDistanceTo_knownCities() {
        val moscow = GPoint(55.7558, 37.6173) // Moscow
        val saintPetersburg = GPoint(59.9343, 30.3351) // Saint Petersburg

        val distance = moscow.distanceTo(saintPetersburg)

        // Expected distance: approximately 633 km
        assertEquals(633.0, distance, 1.0) // Allow a margin of error of 10 km.
    }

    /**
     * Test case for calculating the distance between two points on the equator with different longitudes.
     * The distance between two points on the equator with a 10° difference in longitude should be approximately 1112.0 km.
     */
    @Test
    fun testDistanceTo_equatorPoints() {
        val point1 = GPoint(0.0, 0.0) // Point on the equator and the Prime Meridian
        val point2 = GPoint(0.0, 10.0) // Point on the equator, but 10° east

        val distance = point1.distanceTo(point2)

        // Expected distance: approximately 1112.0 km
        assertEquals(1112.0, distance, 1.0) // Allow a margin of error of 1 km.
    }

    /**
     * Test case for calculating the distance between two random cities (Paris and Los Angeles).
     * The expected distance is approximately 9085.5 km.
     */
    @Test
    fun testDistanceTo_differentLatitudeLongitude() {
        val point1 = GPoint(48.8566, 2.3522) // Paris
        val point2 = GPoint(34.0522, -118.2437) // Los Angeles

        val distance = point1.distanceTo(point2)

        // Expected distance: approximately 9085.5 km
        assertEquals(9085.5, distance, 1.0) // Allow a margin of error of 50 km.
    }
}
