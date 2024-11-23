package com.vazh2100.geoeventapp.data.storages.room

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDataBase? = null

    fun getDatabase(context: Context): AppDataBase {
        return instance ?: synchronized(this) {
            val dateBase = Room.databaseBuilder(
                context.applicationContext, AppDataBase::class.java, "app_database"
            ).build()
            instance = dateBase
            dateBase
        }
    }
}