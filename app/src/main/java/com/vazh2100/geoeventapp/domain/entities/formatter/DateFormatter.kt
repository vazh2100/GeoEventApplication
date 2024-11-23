package com.vazh2100.geoeventapp.domain.entities.formatter

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return this.format(formatter)
}