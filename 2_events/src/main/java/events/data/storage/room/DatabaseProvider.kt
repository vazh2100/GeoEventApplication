package events.data.storage.room

import android.content.Context
import androidx.room.Room

internal object DatabaseProvider {
    private var instance: EventsDatabase? = null

    @Suppress("ReturnCount")
    fun getDatabase(context: Context): EventsDatabase {
        instance?.also { return it }
        synchronized(this) {
            instance?.also { return it }
            val dateBase = Room.databaseBuilder(
                context.applicationContext,
                EventsDatabase::class.java,
                "app_database"
            ).build()
            return dateBase.also {
                instance = it
            }
        }
    }
}
