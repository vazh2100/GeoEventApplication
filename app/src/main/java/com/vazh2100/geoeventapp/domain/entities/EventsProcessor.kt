package com.vazh2100.geoeventapp.domain.entities

import java.time.Instant

object EventsProcessor {

    fun filter(
        events: List<Event>, filter: EventFilter, gPoint: GPoint?
    ): List<Event> = events.filter { it.matchesFilter(filter, gPoint) }

    /**
     * Checks whether the event matches a given filter and is within the specified geographical radius.
     * @param eventFilter The filter criteria.
     * @return True if the event matches all criteria, false otherwise.
     */
    private fun Event.matchesFilter(
        eventFilter: EventFilter, gPoint: GPoint?
    ): Boolean {
        return matchesType(eventFilter.type) && matchesStartDate(eventFilter.startDate) && matchesEndDate(
            eventFilter.endDate
        ) && matchesRadius(eventFilter.radius, gPoint)
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