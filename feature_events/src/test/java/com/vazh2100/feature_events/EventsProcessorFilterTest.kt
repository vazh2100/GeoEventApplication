package com.vazh2100.feature_events

import com.vazh2100.core.domain.entities.GPoint
import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventType
import com.vazh2100.feature_events.domain.entities.event.EventsProcessor
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

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
class EventsProcessorFilterTest {

    // Constants for filtering tests
    private val userGPoint = GPoint(55.0, 37.0)
    private val radius = 65
    private val now = Instant.now()
    val event1 = Event(
        id = 1,
        name = "Concert", // Event type CONCERT
        description = "Description",
        type = EventType.CONCERT,
        latitude = 55.0,
        longitude = 37.01,
        city = "Moscow",
        date = now.minus(1, ChronoUnit.HOURS)
    )
    val event2 = Event(
        id = 2,
        name = "Sport Event", // Event type SPORTING_EVENT
        description = "Description",
        type = EventType.SPORTING_EVENT,
        latitude = 56.0,
        longitude = 38.0,
        city = "Saint-Petersburg",
        date = now.plus(1, ChronoUnit.HOURS)
    )
    val event3 = Event(
        id = 3,
        name = "Festival", // Event type CONCERT
        description = "Description",
        type = EventType.CONCERT,
        latitude = 55.5,
        longitude = 37.5,
        city = "Moscow",
        date = now.plus(1, ChronoUnit.DAYS)
    )
    val event4 = Event(
        id = 4,
        name = "Football Match", // Event type SPORTING_EVENT
        description = "Description",
        type = EventType.SPORTING_EVENT,
        latitude = 60.0,
        longitude = 40.0,
        city = "Somewhere",
        date = now.minus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS)
    )

    // Predefined list of events used in the tests
    private val events = listOf(event1, event2, event3, event4)

    /**
     * Test the filtering of events by type (CONCERT).
     * - Filters events of type CONCERT.
     * - Verifies that the correct number of events (2) of type CONCERT are returned.
     */
    @Test
    fun `test filtering events by type CONCERT`() {
        val eventSearchParams = EventSearchParams(
            type = EventType.CONCERT, startDate = null, endDate = null, radius = null
        )
        val filteredEvents = EventsProcessor.filter(events, eventSearchParams, null)

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
        val eventSearchParams =
            EventSearchParams(type = null, startDate = null, endDate = null, radius = radius)
        val filteredEvents = EventsProcessor.filter(events, eventSearchParams, userGPoint)

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
        val eventSearchParams =
            EventSearchParams(startDate = now, endDate = null, radius = null, type = null)
        val filteredEvents = EventsProcessor.filter(events, eventSearchParams, null)

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
        val eventSearchParams = EventSearchParams(
            type = EventType.CONCERT,
            startDate = now,
            endDate = now.plus(2, ChronoUnit.DAYS),
            radius = radius
        )
        val filteredEvents = EventsProcessor.filter(events, eventSearchParams, userGPoint)
        assertEquals(1, filteredEvents.size)
        assertEquals("Festival", filteredEvents[0].name)
    }

    /**
     * Test filtering events with an invalid date range.
     * - Verifies that no events are returned if startDate > endDate.
     */
    @Test
    fun `test filtering with invalid date range`() {
        val eventSearchParams = EventSearchParams(
            startDate = now.plus(1, ChronoUnit.DAYS),
            endDate = now.minus(1, ChronoUnit.DAYS),
            radius = null,
            type = null
        )
        val filteredEvents = EventsProcessor.filter(events, eventSearchParams, null)
        assertTrue(filteredEvents.isEmpty())
    }
}




