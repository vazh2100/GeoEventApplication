package events.entities

import geolocation.entity.GPoint
import java.time.Instant

internal object EventsProcessor {

    fun List<Event>.searchWith(eventSearchParams: EventSearchParams): List<Event> {
        val filtered = filter(this, eventSearchParams)
        val sorted = sort(filtered, eventSearchParams.sortType, eventSearchParams.gPoint)
        return sorted
    }

    fun sort(
        events: List<Event>,
        sortType: EventSortType?,
        gPoint: GPoint?,
    ): List<Event> = when (sortType) {
        null -> events
        EventSortType.DATE -> events.sortedBy { it.date }
        EventSortType.DISTANCE -> gPoint?.let { events.sortedBy { it.distanceTo(gPoint) } } ?: events
    }

    fun filter(
        events: List<Event>,
        eventSearchParams: EventSearchParams,
    ): List<Event> = events.filter { it.matchesFilter(eventSearchParams) }

    private fun Event.matchesFilter(eventSearchParams: EventSearchParams): Boolean = eventSearchParams.let {
        matchesType(it.type) &&
                matchesStartDate(it.startDate) &&
                matchesEndDate(it.endDate) &&
                matchesRadius(it.radius, it.gPoint)
    }

    private fun Event.matchesType(type: EventType?): Boolean = type == null || this.type == type

    private fun Event.matchesStartDate(startDate: Instant?): Boolean = startDate == null || this.date.isAfter(startDate)

    private fun Event.matchesEndDate(endDate: Instant?): Boolean = endDate == null || this.date.isBefore(endDate)

    private fun Event.matchesRadius(radius: Int?, gPoint: GPoint?): Boolean {
        if (radius == null || gPoint == null) return true
        return this.distanceTo(gPoint) <= radius
    }

    private fun Event.distanceTo(gPoint: GPoint): Double = this.gPoint.distanceTo(gPoint)
}
