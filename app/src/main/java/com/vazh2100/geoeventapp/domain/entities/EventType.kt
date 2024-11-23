package com.vazh2100.geoeventapp.domain.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType(val displayName: String) {
    @SerialName("Concert")
    CONCERT("Concert"),

    @SerialName("Festival")
    FESTIVAL("Festival"),

    @SerialName("Conference")
    CONFERENCE("Conference"),

    @SerialName("Workshop")
    WORKSHOP("Workshop"),

    @SerialName("Seminar")
    SEMINAR("Seminar"),

    @SerialName("Sporting Event")
    SPORTING_EVENT("Sporting Event"),

    @SerialName("Trade Show")
    TRADE_SHOW("Trade Show"),

    @SerialName("Exhibition")
    EXHIBITION("Exhibition")
}
