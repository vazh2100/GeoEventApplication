package events.data.storage.room

import androidx.room.TypeConverter
import events.entities.EventType

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
