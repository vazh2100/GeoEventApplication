package com.vazh2100.feature_events.data.storages.room.typeConverter

import androidx.room.TypeConverter
import com.vazh2100.feature_events.domain.entities.event.EventType

internal class EventTypeConverter {
    @TypeConverter
    fun fromEventType(value: EventType): String {
        return value.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }
}
