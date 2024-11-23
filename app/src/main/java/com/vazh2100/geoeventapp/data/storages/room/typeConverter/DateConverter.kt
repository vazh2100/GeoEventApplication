package com.vazh2100.geoeventapp.data.storages.room.typeConverter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateConverter {

    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME // Формат ISO 8601

    @TypeConverter
    fun fromDate(value: ZonedDateTime): String {
        return value.toInstant().atZone(java.time.ZoneOffset.UTC).format(formatter)
    }

    @TypeConverter
    fun toDate(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value, formatter).toInstant().let { ZonedDateTime.from(it) }
    }
}