package com.vazh2100.geoeventapp.data.storages.room.typeConverter

import androidx.room.TypeConverter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateConverter {

    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @TypeConverter
    fun fromDate(value: ZonedDateTime): String {
        return value.format(formatter)
    }

    @TypeConverter
    fun toDate(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value, formatter)
    }
}