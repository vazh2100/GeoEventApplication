package com.vazh2100.geoeventapp.domain.entities.formatter

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toLocalFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return this.format(formatter)
}

//utc 0
fun Instant.toLocalFormattedString(): String {
    val userZoneId = ZoneId.systemDefault()
    val zonedDateTime = this.atZone(userZoneId)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return zonedDateTime.format(formatter)
}

// местное время
fun ZonedDateTime.toFormattedMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return this.format(formatter)
}

fun Long.toZonedDateTime(): ZonedDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())

}