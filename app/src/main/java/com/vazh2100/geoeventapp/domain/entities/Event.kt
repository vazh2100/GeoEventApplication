package com.vazh2100.geoeventapp.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vazh2100.geoeventapp.domain.entities.json.InstantDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant


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
)
