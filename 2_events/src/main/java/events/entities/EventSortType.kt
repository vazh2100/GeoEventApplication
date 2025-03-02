package events.entities

import core.entities.DisplayNameEnum

internal enum class EventSortType(override val displayName: String) : DisplayNameEnum {
    DATE("Date"),
    DISTANCE("Distance"),
}
