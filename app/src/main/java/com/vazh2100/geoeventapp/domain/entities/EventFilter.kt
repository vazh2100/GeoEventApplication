package com.vazh2100.geoeventapp.domain.entities

import java.time.Instant

data class EventFilter(
    val type: EventType? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val radius: Int? = null
)