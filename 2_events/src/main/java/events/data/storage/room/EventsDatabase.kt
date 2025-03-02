package events.data.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import core.entities.DateConverter
import events.entities.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, EventTypeConverter::class)
internal abstract class EventsDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}
