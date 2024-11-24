package com.vazh2100.geoeventapp.domain.entities.formatter

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


//utc 0
fun Instant.toLocalFormattedString(): String {
    val userZoneId = ZoneId.systemDefault()
    val zonedDateTime = this.atZone(userZoneId)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return zonedDateTime.format(formatter)
}

// utc as utc
fun Instant.formatAsUtc(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    return this.atZone(ZoneId.of("UTC")).format(formatter)
}

///utc milliseconds to utc insrnce
fun Long.toInstance(): Instant {
    return Instant.ofEpochMilli(this)

}