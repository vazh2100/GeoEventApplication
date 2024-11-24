package com.vazh2100.geoeventapp.domain.entity

import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

/**
 * Unit tests for the event filtering functionality in the EventRepository.
 * This test class focuses on validating the filtering of events based on different criteria:
 * 1. Event type (e.g., CONCERT, SPORTING_EVENT)
 * 2. Radius from the user's location
 * 3. Start date of the event
 * 4. End date of the event
 * 5. Combined filtering by type and radius
 * 6. No filters applied, returning all events.
 * The tests are designed to ensure that the `getFilteredEvents` method behaves as expected when
 * filtering events based on one or more criteria.

 * Dependencies:
 * - Koin for dependency injection.
 * - MockK for mocking and spying on methods.
 * - Kotlin Coroutines for asynchronous testing.

 * The test class initializes a mock `EventRepository`, mocks the `getAllEvents` method, and provides
 * a predefined list of events to test the filtering logic.
 */
class EventFilterTest {

    // Constants for filtering tests
    val userLatitude = 55.0
    val userLongitude = 37.0
    val radius = 65
    val now = Instant.now()

    // Predefined list of events used in the tests
    private val events = listOf(
        Event(
            1, "Concert", // Event type CONCERT
            "Description", EventType.CONCERT, 55.0, 37.0, "Moscow", Instant.now()
        ),
        Event(
            2, "Sport Event", // Event type SPORTING_EVENT
            "Description", EventType.SPORTING_EVENT, 56.0, 38.0, "Saint-Petersburg", Instant.now()
        ),
        Event(
            3,
            "Festival", // Event type CONCERT
            "Description",
            EventType.CONCERT,
            55.5,
            37.5,
            "Moscow",
            Instant.now().plus(1, ChronoUnit.DAYS)
        ),
        Event(
            4,
            "Football Match", // Event type SPORTING_EVENT
            "Description",
            EventType.SPORTING_EVENT,
            60.0,
            40.0,
            "Somewhere",
            Instant.now().minus(1, ChronoUnit.DAYS)
        )
    )

    lateinit var spyEventRepository: EventRepository

    /**
     * Sets up the test environment before each test.
     * - Initializes Koin for dependency injection.
     * - Mocks the `getAllEvents` method to return a predefined list of events.
     */
    @Before
    fun setup() {
        // Initializes Koin for the test environment
        startKoin {
            modules(module {
                single<EventRepository> { mockk() }
            })
        }
        val eventRepository: EventRepository = GlobalContext.get().get()
        spyEventRepository = spyk(eventRepository)
        // Mock the `getAllEvents` method within the spy
        every { spyEventRepository["getAllEvents"](true) } returns events
    }

    /**
     * Tears down the test environment after each test.
     * - Stops Koin after all tests are completed.
     */
    @After
    fun teardown() {
        stopKoin()
    }

    /**
     * Test the filtering of events by type (CONCERT).
     * - Filters events of type CONCERT.
     * - Verifies that the correct number of events (2) of type CONCERT are returned.
     */
    @Test
    fun `test filtering events by type CONCERT`() = runBlocking {
        // Filters events by type CONCERT
        val eventFilter =
            EventFilter(type = EventType.CONCERT, startDate = null, endDate = null, radius = null)
        val filteredEvents = spyEventRepository.getFilteredEvents(eventFilter, hasInternet = true)
        // Verifies that two events of type CONCERT are returned
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
    fun `test filtering events by radius`() = runBlocking {
        // Filters events by radius
        val eventFilter =
            EventFilter(type = null, startDate = null, endDate = null, radius = radius)
        val filteredEvents = spyEventRepository.getFilteredEvents(
            eventFilter,
            hasInternet = true,
            userLatitude = userLatitude,
            userLongitude = userLongitude
        )
        // Verifies that two events within the radius are returned
        assertEquals(2, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
        assertEquals("Festival", filteredEvents[1].name)
    }

    /**
     * Test the filtering of events by start date.
     * - Filters events that start after the current date.
     * - Verifies that the correct number of future events (3) are returned.
     */
    @Test
    fun `test filtering events by start date`() = runBlocking {
        // Filters events that start after the current date
        val eventFilter = EventFilter(startDate = now, endDate = null, radius = null, type = null)
        val filteredEvents = spyEventRepository.getFilteredEvents(eventFilter, hasInternet = true)
        // Verifies that three events starting after the current date are returned
        assertEquals(3, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
        assertEquals("Sport Event", filteredEvents[1].name)
        assertEquals("Festival", filteredEvents[2].name)
    }

    /**
     * Test the filtering of events by end date.
     * - Filters events that end before the current date.
     * - Verifies that the correct number of past events (1) are returned.
     */
    @Test
    fun `test filtering events by end date`() = runBlocking {
        // Filters events that end before the current date
        val eventFilter = EventFilter(startDate = null, endDate = now, radius = null, type = null)
        val filteredEvents = spyEventRepository.getFilteredEvents(eventFilter, hasInternet = true)
        // Verifies that one event that ended before the current date is returned
        assertEquals(1, filteredEvents.size)
        assertEquals("Football Match", filteredEvents[0].name)
    }

    /**
     * Test the filtering of events by both type and radius.
     * - Filters events of type CONCERT within a specified radius.
     * - Verifies that the correct number of events (2) are returned.
     */
    @Test
    fun `test filtering events by type and radius`() = runBlocking {
        // Filters events by type and radius
        val eventFilter = EventFilter(type = EventType.CONCERT, startDate = null, endDate = null, radius = radius)
        val filteredEvents = spyEventRepository.getFilteredEvents(eventFilter, hasInternet = true)
        // Verifies that two events of type CONCERT within the radius are returned
        assertEquals(2, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
        assertEquals("Festival", filteredEvents[1].name)
    }

    /**
     * Test the filtering of events with no filters applied.
     * - Verifies that all events are returned when no filters are provided.
     */
    @Test
    fun `test filtering events with no filters`() = runBlocking {
        // No filters — all events should be returned
        val eventFilter = EventFilter(null, null, null, null)
        val filteredEvents = spyEventRepository.getFilteredEvents(eventFilter, hasInternet = true)
        // Verifies that all four events are returned
        assertEquals(4, filteredEvents.size)
    }
}



