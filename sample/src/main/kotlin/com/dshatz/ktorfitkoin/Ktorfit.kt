package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.annotation.ServiceModule
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
fun json(): Json {
    return Json
}

@Single
fun http(json: Json): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }
}

@Single
fun ktorFit(http: HttpClient): Ktorfit {
    return Ktorfit.Builder().httpClient(http).build()
}

@ServiceModule("com.dshatz.ktorfitkoin.service")
class KtorfitModule {

}