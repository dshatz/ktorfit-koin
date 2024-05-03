package com.dshatz.ktorfitkoin.service

import de.jensklingenberg.ktorfit.http.GET

interface IPService {

    @GET("https://api.ipify.org")
    suspend fun getIP(): String

    @GET("https://api6.ipify.org")
    suspend fun getIP6(): String

}