package com.vazh2100.feature_events.domain.entities.event

import com.vazh2100.core.entities.DisplayNameEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EventType(override val displayName: String) : DisplayNameEnum {
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
