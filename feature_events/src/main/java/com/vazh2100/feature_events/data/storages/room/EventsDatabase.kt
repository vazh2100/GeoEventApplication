package com.vazh2100.feature_events.data.storages.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vazh2100.core.data.storages.room.typeConverter.DateConverter
import com.vazh2100.feature_events.data.storages.room.dao.EventDao
import com.vazh2100.feature_events.data.storages.room.typeConverter.EventTypeConverter
import com.vazh2100.feature_events.domain.entities.event.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, EventTypeConverter::class)
internal abstract class EventsDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}