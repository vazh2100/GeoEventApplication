package com.vazh2100.geoeventapp.data.storages.room

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val dateBase = Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "app_database"
            ).build()
            instance = dateBase
            dateBase
        }
    }
}