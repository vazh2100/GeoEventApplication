package com.vazh2100.geoeventapp.data.storages.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.data.storages.room.typeConverter.DateConverter
import com.vazh2100.geoeventapp.data.storages.room.typeConverter.EventTypeConverter
import com.vazh2100.geoeventapp.domain.entities.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, EventTypeConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}