package com.vazh2100.feature_events.domain.entities.event

import com.vazh2100.core.entities.DisplayNameEnum

internal enum class EventSortType(override val displayName: String) : DisplayNameEnum {
    DATE("Date"),
    DISTANCE("Distance"),
}
