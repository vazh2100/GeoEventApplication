package events

import android.util.Log
import events.data.repository.EventRepository
import events.data.storage.EventsPreferencesStorage
import events.entities.Event
import events.entities.EventSearchParams
import events.entities.EventSortType
import events.entities.EventType
import events.entities.EventsProcessor
import events.entities.EventsProcessor.searchWith
import events.usecase.GetFilteredEventsUseCase
import geolocation.entity.GPoint
import geolocation.usecase.IGetLocationStatusUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import network.entity.NetworkStatus
import network.usecase.IObserveNetworkStateUseCase
import org.junit.Before
import org.junit.Test
import java.time.Instant

/**
 * Unit tests for the GetFilteredEventsUseCase class.
 * These tests ensure that the GetFilteredEventsUseCase behaves correctly in different scenarios.
 */
internal class GetFilteredEventsUseCaseTest {

    private lateinit var getFilteredEvents: GetFilteredEventsUseCase
    private val eventRepository: EventRepository = mockk()
    private val preferencesStorage: EventsPreferencesStorage = mockk()
    private val getNetworkStatus: IObserveNetworkStateUseCase = mockk()
    private val getLocationStatus: IGetLocationStatusUseCase = mockk()
    private val eventSearchParams = EventSearchParams(radius = 5000, sortType = EventSortType.DISTANCE)
    private val now = Instant.now()
    private val userGPoint = GPoint(0.0, 0.0)
    private val event1 = Event(
        id = 1,
        name = "Concert", // Event type CONCERT
        description = "Description",
        type = EventType.CONCERT,
        latitude = 55.0,
        longitude = 37.01,
        city = "Moscow",
        date = now
    )
    private val event2 = Event(
        id = 2,
        name = "Sport Event", // Event type SPORTING_EVENT
        description = "Description",
        type = EventType.SPORTING_EVENT,
        latitude = 56.0,
        longitude = 38.0,
        city = "Saint-Petersburg",
        date = now
    )
    private val mockEvents = listOf(event1, event2, event1, event2)
    private val searchedEvents = listOf(event1, event2)

    @Before
    fun setUp() {
        getFilteredEvents = GetFilteredEventsUseCase(
            eventRepository,
            preferencesStorage,
            getNetworkStatus,
            getLocationStatus
        )
        mockkObject(EventsProcessor)
        mockkStatic(Log::class)
        coEvery { preferencesStorage.saveEventSearchParams(eventSearchParams) } just Runs
        every { getNetworkStatus() } returns flowOf(NetworkStatus.CONNECTED)
        every { getLocationStatus.userGPoint.value } returns userGPoint
        coEvery { eventRepository.getAllEvents(true) } returns mockEvents
        coEvery { eventRepository.getAllEvents(false) } returns mockEvents
        every { mockEvents.searchWith(eventSearchParams, userGPoint) } returns searchedEvents
        every { Log.e(any(), any(), any()) } returns 0
    }

    /**
     * Test case for successful retrieval of events when there is an internet connection.
     * Verifies that all dependencies are correctly called and the result contains the expected filtered events.
     */
    @Test
    fun `test get events successfully with network`() = runBlocking {
        val result = getFilteredEvents(eventSearchParams)

        coVerify { preferencesStorage.saveEventSearchParams(eventSearchParams) }
        coVerify { getNetworkStatus() }
        coVerify { getLocationStatus.userGPoint.value }
        coVerify { eventRepository.getAllEvents(true) }
        assert(result.isSuccess)
        assertEquals(searchedEvents, result.getOrNull())
    }

    /**
     * Test case for successful retrieval of events when there is no internet connection.
     * Verifies that the events are fetched from the local repository
     * and the result contains the expected filtered events.
     */
    @Test
    fun `test get events successfully with no network`() = runBlocking {
        coEvery { getNetworkStatus() } returns flowOf(NetworkStatus.DISCONNECTED)
        val result = getFilteredEvents(eventSearchParams)

        coVerify { preferencesStorage.saveEventSearchParams(eventSearchParams) }
        coVerify { getNetworkStatus() }
        coVerify { getLocationStatus.userGPoint.value }
        coVerify { eventRepository.getAllEvents(false) }
        assert(result.isSuccess)
        assertEquals(searchedEvents, result.getOrNull())
    }

    /**
     * Test case to handle errors when fetching events from the repository.
     * Verifies that the use case correctly handles exceptions and returns a failure result.
     */
    @Test
    fun `test error when fetching events`(): Unit = runBlocking {
        coEvery { eventRepository.getAllEvents(true) } throws Exception("Network Error")
        val result = getFilteredEvents(eventSearchParams)

        coVerify { preferencesStorage.saveEventSearchParams(eventSearchParams) }
        coVerify { getNetworkStatus() }
        coVerify { getLocationStatus.userGPoint.value }
        coVerify { eventRepository.getAllEvents(true) }
        assert(result.isFailure)
        assertEquals("Failed to get events", result.exceptionOrNull()?.message)
    }

    /**
     * Test case for handling errors when saving event search parameters in preferences storage.
     * Verifies that if an error occurs while saving search parameters, the events are still fetched correctly
     * and the result is a success with the correct events.
     */
    @Test
    fun `test error when save event search params`() = runBlocking {
        // Simulate an error when saving event search parameters
        coEvery { preferencesStorage.saveEventSearchParams(eventSearchParams) } throws Exception("Storage Error")
        // Execute the use case
        val result = getFilteredEvents(eventSearchParams)
        // Verify that all the necessary methods were called
        coVerify { preferencesStorage.saveEventSearchParams(eventSearchParams) }
        coVerify { getNetworkStatus() }
        coVerify { getLocationStatus.userGPoint.value }
        coVerify { eventRepository.getAllEvents(true) }
        // Verify that despite the error in saving the search parameters, the events are correctly fetched
        assert(result.isSuccess)
        assertEquals(searchedEvents, result.getOrNull())
    }
}
