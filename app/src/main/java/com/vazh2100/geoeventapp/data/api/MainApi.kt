package com.vazh2100.geoeventapp.data.api

import com.vazh2100.geoeventapp.domain.entities.Event
import retrofit2.http.GET

interface MainApi {
    @GET("events")
    suspend fun getEvents(): List<Event>
}
