package events.entities

import geolocation.entity.GPoint
import java.time.Instant

internal data class EventSearchParams(
    val type: EventType? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val radius: Int? = null,
    var gPoint: GPoint? = null,
    val sortType: EventSortType? = null,
) {
    val hasGeoFilter: Boolean get() = radius != null || sortType == EventSortType.DISTANCE
}
