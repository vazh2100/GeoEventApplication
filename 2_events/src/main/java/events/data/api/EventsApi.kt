package events.data.api

import events.entities.Event
import retrofit2.http.GET

internal interface EventsApi {

    @GET("events")
    suspend fun getEvents(): List<Event>
}
