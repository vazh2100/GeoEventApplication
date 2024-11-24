package com.vazh2100.geoeventapp.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vazh2100.geoeventapp.domain.entities.json.InstantDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@Serializable
@Entity(tableName = "events")
data class Event(
    @SerialName("id") @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @SerialName("name") @ColumnInfo(name = "name") val name: String,
    @SerialName("description") @ColumnInfo(name = "description") val description: String,
    @SerialName("type") @ColumnInfo(name = "type") val type: EventType,
    @SerialName("latitude") @ColumnInfo(name = "latitude") val latitude: Double,
    @SerialName("longitude") @ColumnInfo(name = "longitude") val longitude: Double,
    @SerialName("city") @ColumnInfo(name = "city") val city: String,
    @Serializable(with = InstantDateTimeSerializer::class) @SerialName("date") @ColumnInfo(name = "date") val date: Instant
) {


    /**
     * Checks whether the event matches a given filter and is within the specified geographical radius.
     * @param eventFilter The filter criteria.
     * @param userLatitude The latitude of the user.
     * @param userLongitude The longitude of the user.
     * @return True if the event matches all criteria, false otherwise.
     */
    fun matchesFilter(
        eventFilter: EventFilter,
        userLatitude: Double?,
        userLongitude: Double?
    ): Boolean {
        return matchesType(eventFilter.type) &&
                matchesStartDate(eventFilter.startDate) &&
                matchesEndDate(eventFilter.endDate) &&
                matchesRadius(eventFilter.radius, userLatitude, userLongitude)
    }

    fun matchesType(type: EventType?): Boolean {
        return type == null || this.type == type
    }

    fun matchesStartDate(startDate: Instant?): Boolean {
        return startDate == null || this.date.isAfter(startDate)
    }

    fun matchesEndDate(endDate: Instant?): Boolean {
        return endDate == null || this.date.isBefore(endDate)
    }

    fun matchesRadius(
        radius: Int?, userLatitude: Double?, userLongitude: Double?
    ): Boolean {
        if (radius == null || userLatitude == null || userLongitude == null) return true
        return distanceFrom(userLatitude, userLongitude) <= radius
    }


    /**
     * Calculates the geographical distance between the event and a given point using the Haversine formula.
     * @param lat2 The latitude of the point.
     * @param lon2 The longitude of the point.
     * @return The distance in kilometers.
     */
    fun distanceFrom(lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - this.latitude)
        val dLon = Math.toRadians(lon2 - this.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

}
