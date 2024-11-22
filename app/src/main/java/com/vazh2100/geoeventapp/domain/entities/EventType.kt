package com.vazh2100.geoeventapp.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Enum для типа события
@Serializable
enum class EventType {
    @SerialName("Concert")
    CONCERT,

    @SerialName("Festival")
    FESTIVAL,

    @SerialName("Conference")
    CONFERENCE,

    @SerialName("Workshop")
    WORKSHOP,

    @SerialName("Seminar")
    SEMINAR,

    @SerialName("Sporting Event")
    SPORTING_EVENT,

    @SerialName("Trade Show")
    TRADE_SHOW,

    @SerialName("Exhibition")
    EXHIBITION
}