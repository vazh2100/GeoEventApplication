package events.entities

import java.time.Instant

internal data class EventSearchParams(
    val type: EventType? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val radius: Int? = null,
    val sortType: EventSortType? = null,
) {
    val hasGeoFilter: Boolean get() = radius != null || sortType == EventSortType.DISTANCE
}
