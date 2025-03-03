package events

import events.entities.Event
import events.entities.EventSearchParams
import events.entities.EventType
import events.entities.EventsProcessor
import geolocation.entity.GPoint
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

internal class EventsProcessorFilterTest {

    private val userGPoint = GPoint(55.0, 37.0)
    private val radius = 65
    private val now = Instant.now()
    private val event1 = Event(
        id = 1,
        name = "Concert",
        description = "",
        type = EventType.CONCERT,
        latitude = 55.0,
        longitude = 37.01,
        city = "",
        date = now.minus(1, ChronoUnit.HOURS)
    )
    private val event2 = Event(
        id = 2,
        name = "Sport Event",
        description = "",
        type = EventType.SPORTING_EVENT,
        latitude = 56.0,
        longitude = 38.0,
        city = "",
        date = now.plus(1, ChronoUnit.HOURS)
    )
    private val event3 = Event(
        id = 3,
        name = "",
        description = "",
        type = EventType.CONCERT,
        latitude = 55.5,
        longitude = 37.5,
        city = "",
        date = now.plus(1, ChronoUnit.DAYS)
    )
    private val event4 = Event(
        id = 4,
        name = "",
        description = "",
        type = EventType.SPORTING_EVENT,
        latitude = 60.0,
        longitude = 40.0,
        city = "",
        date = now.minus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS)
    )

    // Predefined list of events used in the tests
    private val events = listOf(event1, event2, event3, event4)

    private val cases = listOf(
        Case(events, EventSearchParams(type = EventType.CONCERT), listOf(event1, event3)),
        Case(events, EventSearchParams(radius = radius), listOf(event1, event3)),
        Case(events, EventSearchParams(startDate = now), listOf(event2, event3)),
        Case(
            events,
            EventSearchParams(EventType.CONCERT, startDate = now, endDate = now.plus(2, ChronoUnit.DAYS), radius),
            listOf(event3)
        ),
        // invalid ranges
        Case(
            events,
            EventSearchParams(startDate = now.plus(1, ChronoUnit.DAYS), endDate = now.minus(1, ChronoUnit.DAYS)),
            listOf()
        ),
    )

    @Test
    fun `when the input and the filter are in place, the result is expected`() {
        for ((input, filter, expected) in cases) {
            assertEquals(expected, EventsProcessor.filter(input, filter, userGPoint))
        }
    }

    data class Case(val input: List<Event>, val filter: EventSearchParams, val expected: List<Event>)
}
