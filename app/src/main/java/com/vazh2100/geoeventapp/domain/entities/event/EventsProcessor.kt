package com.vazh2100.geoeventapp.domain.entities.event

import com.vazh2100.geoeventapp.domain.entities.GPoint
import java.time.Instant

object EventsProcessor {

    fun sort(events: List<Event>, sortType: EventSortType?, gPoint: GPoint?): List<Event> {
        return when (sortType) {
            null -> events
            EventSortType.DATE -> events.sortedBy { it.date }
            EventSortType.DISTANCE -> gPoint?.let {
                events.sortedBy { it.distanceTo(gPoint) }
            } ?: events
        }
    }

    fun filter(
        events: List<Event>, eventSearchParams: EventSearchParams, gPoint: GPoint?
    ): List<Event> = events.filter { it.matchesFilter(eventSearchParams, gPoint) }



    /**
     * Checks whether the event matches a given filter and is within the specified geographical radius.
     * @param eventSearchParams The filter criteria.
     * @return True if the event matches all criteria, false otherwise.
     */
    private fun Event.matchesFilter(
        eventSearchParams: EventSearchParams, gPoint: GPoint?
    ): Boolean {
        return matchesType(eventSearchParams.type) && matchesStartDate(eventSearchParams.startDate) && matchesEndDate(
            eventSearchParams.endDate
        ) && matchesRadius(eventSearchParams.radius, gPoint)
    }

    private fun Event.matchesType(type: EventType?): Boolean = type == null || this.type == type

    private fun Event.matchesStartDate(startDate: Instant?): Boolean =
        startDate == null || this.date.isAfter(startDate)

    private fun Event.matchesEndDate(endDate: Instant?): Boolean =
        endDate == null || this.date.isBefore(endDate)

    private fun Event.matchesRadius(
        radius: Int?, gPoint: GPoint?
    ): Boolean {
        if (radius == null || gPoint == null) return true
        return this.distanceTo(gPoint) <= radius
    }

    /**
     * Calculates the geographical distance between the event and a given point using the Haversine formula.
     * @return The distance in kilometers.
     */
    private fun Event.distanceTo(gPoint: GPoint): Double = this.gPoint.distanceTo(gPoint)
}

fun List<Event>.searchWith(
    eventSearchParams: EventSearchParams, gPoint: GPoint?
): List<Event> {
    val filtered = EventsProcessor.filter(this, eventSearchParams, gPoint)
    val sorted = EventsProcessor.sort(filtered, eventSearchParams.sortType, gPoint)
    return sorted
}