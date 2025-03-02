package com.vazh2100.core.entities

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateFormatter {
    /**
     * Converts an [Instant] to a local formatted string representation.
     * The string is formatted using the system's default time zone.
     * @return A string representation of the instant in the format "dd MMM yyyy, HH:mm".
     */
    fun Instant.toLocalFormattedString(): String {
        val userZoneId = ZoneId.systemDefault()
        val zonedDateTime = this.atZone(userZoneId)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        return zonedDateTime.format(formatter)
    }

    /**
     * Formats an [Instant] as a UTC-based string.
     * The resulting string represents the date and time in UTC.
     * @return A string representation of the instant in UTC format "dd MMM yyyy".
     */
    fun Instant.formatAsUtc(): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return this.atZone(ZoneId.of("UTC")).format(formatter)
    }

    /**
     * Converts a timestamp in milliseconds since the epoch to an [Instant].
     * @return The corresponding [Instant] object.
     */
    fun Long.toInstance(): Instant {
        return Instant.ofEpochMilli(this)
    }
}
