package com.vazh2100.core.data.storages.room.type_converter

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Provides methods for converting `Instant` to `String` and vice versa.
 * Used by Room to store and retrieve `Instant` values in the database.
 */
class DateConverter {

    @TypeConverter
    fun fromDate(value: Instant): String {
        return value.toString()
    }

    @TypeConverter
    fun toDate(value: String): Instant {
        return Instant.parse(value)
    }
}
