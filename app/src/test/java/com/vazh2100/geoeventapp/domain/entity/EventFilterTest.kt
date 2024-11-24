import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.time.Instant
import kotlin.test.assertEquals

class EventFilterTest {

    @Before
    fun setup() {
        // Инициализация Koin для тестов
        startKoin {
            modules(
                module {
                    single<EventRepository> { mockk() }
                }
            )
        }
    }

    @After
    fun teardown() {
        // Остановка Koin после тестов
        stopKoin()
    }

    @Test
    fun `test filtering events by type`() = runBlocking {
        val eventRepository: EventRepository = GlobalContext.get().get()

        val events = listOf(
            Event(
                1,
                "Concert",
                "Description",
                EventType.CONCERT,
                55.0,
                37.0,
                "Moscow",
                Instant.now()
            ),
            Event(
                2,
                "Sport Event",
                "Description",
                EventType.SPORTING_EVENT,
                56.0,
                38.0,
                "Saint-Petersburg",
                Instant.now()
            )
        )

        every { eventRepository["getAllEvents"](true) } returns events

        val eventFilter =
            EventFilter(type = EventType.CONCERT, startDate = null, endDate = null, radius = null)
        val filteredEvents = events.filter { it.matchesFilter(eventFilter, null, null) }

        // Проверка
        assertEquals(1, filteredEvents.size)
        assertEquals("Concert", filteredEvents[0].name)
    }

    @Test
    fun `test filtering events by radius`() = runBlocking {
        // Получаем зависимость через Koin
        GlobalContext.get().get()

        val userLatitude = 55.0
        val userLongitude = 37.0
        val radius = 64

        // Создаем события
        val events = listOf(
            Event(
                1,
                "Nearby Event",
                "Description",
                EventType.CONCERT,
                55.5,
                37.5,
                "Moscow",
                Instant.now()
            ), //64 km
            Event(
                2,
                "Far Away Event",
                "Description",
                EventType.CONCERT,
                60.0,
                40.0,
                "Somewhere",
                Instant.now()
            ) // 584 km
        )

        // Применяем фильтр с радиусом
        val eventFilter = EventFilter(null, null, null, radius)

        // Фильтруем события
        val filteredEvents =
            events.filter { it.matchesFilter(eventFilter, userLatitude, userLongitude) }

        // Проверка
        assertEquals(1, filteredEvents.size)
        assertEquals("Nearby Event", filteredEvents[0].name)
    }
}
