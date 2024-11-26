package com.vazh2100.geoeventapp.domain.entity

import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import com.vazh2100.geoeventapp.domain.entities.EventsProcessor
import com.vazh2100.geoeventapp.domain.entities.GPoint
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

/**
 * Unit tests for the event filtering functionality in the EventsProcessor.
 * This test class focuses on validating the filtering of events based on different criteria:
 * 1. Event type (e.g., CONCERT, SPORTING_EVENT)
 * 2. Radius from the user's location
 * 3. Start date of the event
 * 4. End date of the event
 * 5. Combined filtering by type and radius
 * 6. No filters applied, returning all events.
 */
class EventsProcessorTest {

    // Constants for filtering tests
    private val userGPoint = GPoint(55.0, 37.0)
    private val radius = 65
    private val now = Instant.now()

    // Predefined list of events used in the tests
    private val events = listOf(
        Event(
            1, "Concert", // Event type CONCERT
            "Description", EventType.CONCERT, 55.0, 37.01, "Moscow", now.minus(1, ChronoUnit.HOURS)
        ), Event(
            2,
            "Sport Event", // Event type SPORTING_EVENT
            "Description",
            EventType.SPORTING_EVENT,
            56.0,
            38.0,
            "Saint-Petersburg",
            now.plus(1, ChronoUnit.HOURS)
        ), Event(
            3, "Festival", // Event type CONCERT
            "Description", EventType.CONCERT, 55.5, 37.5, "Moscow", now.plus(1, ChronoUnit.DAYS)
        ), Event(
            4,
            "Football Match", // Event type SPORTING_EVENT
            "Description",
            EventType.SPORTING_EVENT,
            60.0,
            40.0,
            "Somewhere",
            now.minus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS)
        )
    )

    /**
     * Test the filtering of events by type (CONCERT).
     * - Filters events of type CONCERT.
     * - Verifies that the correct number of events (2) of type CONCERT are returned.
     */
    @Test
    fun `test filtering events by type CONCERT`() {
        val eventFilter =
            EventFilter(type = EventType.CONCERT, startDate = null, endDate = null, radius = null)
        val filteredEvents = EventsProcessor.filter(events, eventFilter, null)

        assertEquals(2, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
        assertEquals("Festival", filteredEvents[1].name)
    }

    /**
     * Test the filtering of events by radius.
     * - Filters events within a specified radius from the user's location.
     * - Verifies that the correct number of events (2) within the radius are returned.
     */
    @Test
    fun `test filtering events by radius`() {
        val eventFilter =
            EventFilter(type = null, startDate = null, endDate = null, radius = radius)
        val filteredEvents = EventsProcessor.filter(events, eventFilter, userGPoint)

        assertEquals(2, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
        assertEquals("Festival", filteredEvents[1].name)
    }

    /**
     * Test the filtering of events by start date.
     * - Filters events that start after the current date.
     * - Verifies that the correct number of future events (2) are returned.
     */
    @Test
    fun `test filtering events by start date`() {
        val eventFilter = EventFilter(startDate = now, endDate = null, radius = null, type = null)
        val filteredEvents = EventsProcessor.filter(events, eventFilter, null)

        assertEquals(2, filteredEvents.size)
        assertEquals("Sport Event", filteredEvents[0].name)
        assertEquals("Festival", filteredEvents[1].name)
    }

    /**
     * Test filtering events by type, radius, start date, and end date.
     * - Filters events of type CONCERT within radius and after the current date.
     * - Verifies that the correct event is returned.
     */
    @Test
    fun `test filtering events by type, radius, start date, and end date`() {
        val eventFilter = EventFilter(
            type = EventType.CONCERT,
            startDate = now,
            endDate = now.plus(2, ChronoUnit.DAYS),
            radius = radius
        )
        val filteredEvents = EventsProcessor.filter(events, eventFilter, userGPoint)
        println(events)
        println(filteredEvents)

        assertEquals(1, filteredEvents.size)
        assertEquals("Festival", filteredEvents[0].name)
    }

    /**
     * Test filtering events with an invalid date range.
     * - Verifies that no events are returned if startDate > endDate.
     */
    @Test
    fun `test filtering with invalid date range`() {
        val eventFilter = EventFilter(
            startDate = now.plus(1, ChronoUnit.DAYS),
            endDate = now.minus(1, ChronoUnit.DAYS),
            radius = null,
            type = null
        )
        val filteredEvents = EventsProcessor.filter(events, eventFilter, null)
        assertTrue(filteredEvents.isEmpty())
    }
}




