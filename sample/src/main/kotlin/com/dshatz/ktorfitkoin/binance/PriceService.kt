package com.dshatz.ktorfitkoin.binance

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.serialization.Serializable

interface PriceService {
    @GET("https://api.binance.com/api/v3/ticker/price")
    suspend fun getCurrentPrice(@Query("symbol") symbol: String): Response

    @GET("https://api.binance.com/api/v3/ticker/price")
    suspend fun getCurrentPrices(): List<Response>
}

@Serializable
data class Response(val symbol: String, val price: Double)