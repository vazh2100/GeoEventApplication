package com.vazh2100.core.domain.entities

enum class LocationStatus(val statusMessage: String) {
    PERMISSION_DENIED("Location permission denied"),
    PERMISSION_GRANTED("Location permission granted."),
    LOCATION_OFF("Location is off."),
    LOCATION_ON("Location is on."),
    UNDEFINED("")
}