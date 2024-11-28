package com.vazh2100.feature_events.data.network.api

import com.vazh2100.feature_events.domain.entities.event.Event
import retrofit2.http.GET

internal interface EventsApi {

    @GET("events")
    suspend fun getEvents(): List<Event>
}
