package com.dshatz.ktorfitkoin.notscanned

import de.jensklingenberg.ktorfit.http.GET
import kotlinx.serialization.Serializable

interface SunService {
    @GET("https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400")
    suspend fun getSunriseSunset(): Response
}

@Serializable
data class Response(
    val results: ResponseResults
)

@Serializable
data class ResponseResults(
    val sunrise: String,
    val sunset: String,
)