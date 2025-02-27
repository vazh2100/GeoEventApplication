package com.vazh2100.feature_events

import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.feature_events.domain.entities.event.EventSortType
import com.vazh2100.feature_events.domain.entities.event.EventType
import com.vazh2100.feature_events.domain.entities.event.EventsProcessor
import com.vazh2100.geolocation.entity.GPoint
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant

/**
 * Unit tests for the EventsProcessor's sort function.
 * Verifies that events are correctly sorted based on different criteria such as date and distance.
 */
internal class EventsProcessorSortTest {

    // Test events used for sorting
    private val event1 = Event(
        id = 1,
        name = "Event 1",
        description = "Description 1",
        type = EventType.CONCERT,
        latitude = 40.0,
        longitude = 30.0,
        city = "City 1",
        date = Instant.parse("2024-12-01T10:00:00Z")
    )
    private val event2 = Event(
        id = 2,
        name = "Event 2",
        description = "Description 2",
        type = EventType.FESTIVAL,
        latitude = 42.0,
        longitude = 32.0,
        city = "City 2",
        date = Instant.parse("2024-11-01T10:00:00Z")
    )
    private val event3 = Event(
        id = 3,
        name = "Event 3",
        description = "Description 3",
        type = EventType.SEMINAR,
        latitude = 41.0,
        longitude = 31.0,
        city = "City 3",
        date = Instant.parse("2024-10-01T10:00:00Z")
    )

    // List of events used for testing
    private val events = listOf(event1, event2, event3)

    /**
     * Test that events are correctly sorted by date in ascending order.
     */
    @Test
    fun `sort by date`() {
        // Sorting by DATE
        val sortedEvents = EventsProcessor.sort(events, EventSortType.DATE, null)
        // Verify that events are sorted by date in ascending order
        assertEquals(listOf(event3, event2, event1), sortedEvents)
    }

    /**
     * Test that events are correctly sorted by distance from a specified location.
     */
    @Test
    fun `sort by distance`() {
        val userGPoint = GPoint(40.0, 30.0) // User's location for distance calculation
        // Sorting by DISTANCE
        val sortedEvents = EventsProcessor.sort(events, EventSortType.DISTANCE, userGPoint)
        // Verify that events are sorted by distance from the user's location
        assertEquals(listOf(event1, event3, event2), sortedEvents)
    }

    /**
     * Test that the original list is returned when the sort type is null (no sorting applied).
     */
    @Test
    fun `no sorting when sortType is null`() {
        // Sorting with null sortType
        val sortedEvents = EventsProcessor.sort(events, null, null)
        // Verify that the original order of events is preserved
        assertEquals(events, sortedEvents)
    }

    /**
     * Test that sorting by distance with a null location returns the original list.
     */
    @Test
    fun `sort with null gPoint for distance sorting`() {
        // Sorting by DISTANCE with a null gPoint
        val sortedEvents = EventsProcessor.sort(events, EventSortType.DISTANCE, null)
        // Verify that the original order of events is preserved
        assertEquals(events, sortedEvents)
    }
}
