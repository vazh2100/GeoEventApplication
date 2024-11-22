package com.vazh2100.geoeventapp.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

// Data class для события
@Serializable
data class Event(
    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String,

    @SerialName("type")
    val type: EventType,

    @SerialName("latitude")
    val latitude: Double,

    @SerialName("longitude")
    val longitude: Double,

    @SerialName("city")
    val city: String,

    @SerialName("date")
//    @Serializable(DateTimeSerializer::class)  // Используем сериализацию/десериализацию даты
    val date: Date
)
