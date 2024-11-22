package com.vazh2100.geoeventapp.data.storages.room.typeConverter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class DateConverter {

    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME // Формат ISO 8601

    @TypeConverter
    fun fromDate(value: Date): String {
        return value.toInstant().atZone(java.time.ZoneOffset.UTC).format(formatter)
    }

    @TypeConverter
    fun toDate(value: String): Date {
        return ZonedDateTime.parse(value, formatter).toInstant().let { Date.from(it) }
    }
}