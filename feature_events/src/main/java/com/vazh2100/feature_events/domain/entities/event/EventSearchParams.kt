package com.vazh2100.feature_events.domain.entities.event

import java.time.Instant

internal data class EventSearchParams(
    val type: EventType? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val radius: Int? = null,
    val sortType: EventSortType? = null,
)