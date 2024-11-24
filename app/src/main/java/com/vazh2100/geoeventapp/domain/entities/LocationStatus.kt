package com.vazh2100.geoeventapp.domain.entities

enum class LocationStatus(val statusMessage: String) {
    PERMISSION_DENIED("Permission denied. Please enable location permissions."),
    PERMISSION_GRANTED("Location permissions granted."),
    LOCATION_OFF("Location is off. Please turn on location."),
    LOCATION_ON("Location is on."),
}