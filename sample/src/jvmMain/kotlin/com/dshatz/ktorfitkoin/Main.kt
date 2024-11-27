package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.notscanned.SunService
import com.dshatz.ktorfitkoin.service.IPService
import com.dshatz.ktorfitkoin.service.PriceService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import java.net.SocketException


/**
 * This will fail if KOIN_CONFIG_CHECK == true. SunService is not provided.
 * A warning will be printed by the kspKotlin task.
 * Uncomment this to test that is the case.
 */
/*@Single
class SunRepo(private val service: SunService) {
    suspend fun getSunsetTime(): String = service.getSunriseSunset().results.sunset
    suspend fun getSunrise(): String = service.getSunriseSunset().results.sunrise
}*/

fun main() {
    startKoin {
        modules(modules = listOf(com.dshatz.ktorfitkoin.Module().module))
    }

    val service: PriceService = get().get()
    val ipService: IPService by get().inject()
//    val sunRepo: SunRepo by get().inject()

    runBlocking {
        println("BTC Price: ${service.getCurrentPrice("BTCEUR").price} EUR")
        println("Current IPv4: ${ipService.getIP()}")
        println("Current IPv6: ${
            kotlin.runCatching { ipService.getIP6() }.recover { if (it is SocketException) "Not supported" else throw it }.getOrThrow()
        }")
//        println("Sunset: ${sunRepo.getSunsetTime()}")
    }
}

@Module(includes = [KtorfitModule::class])
@ComponentScan("com.dshatz.ktorfitkoin")
class Module