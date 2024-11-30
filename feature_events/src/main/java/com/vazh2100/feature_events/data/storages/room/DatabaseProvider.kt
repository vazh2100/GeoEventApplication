package com.vazh2100.feature_events.data.storages.room

import android.content.Context
import androidx.room.Room

internal object DatabaseProvider {

    private var instance: EventsDatabase? = null
    fun getDatabase(context: Context): EventsDatabase {
        return instance ?: synchronized(this) {
            val dateBase = Room.databaseBuilder(
                context.applicationContext,
                EventsDatabase::class.java,
                "app_database"
            ).build()
            instance = dateBase
            dateBase
        }
    }
}
