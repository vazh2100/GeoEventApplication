package com.vazh2100.geoeventapp.data.storages.room.typeConverter

import androidx.room.TypeConverter
import com.vazh2100.geoeventapp.domain.entities.EventType

class EventTypeConverter {
    @TypeConverter
    fun fromEventType(value: EventType): String {
        return value.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }
}