package com.vazh2100.core.data.storages.room.typeConverter

import androidx.room.TypeConverter
import java.time.Instant

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