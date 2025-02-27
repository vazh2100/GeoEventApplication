package com.vazh2100.geolocation.entity

enum class LocationStatus(val statusMessage: String) {
    PERMISSION_DENIED("Location permission denied."),
    LOCATION_OFF("Location is off."),
    LOCATION_ON("Location is on."),
    UNDEFINED("")
}
