package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.service.IPService
import com.dshatz.ktorfitkoin.service.PriceService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import java.net.SocketException


fun main() {
    startKoin {
        modules(modules = listOf(com.dshatz.ktorfitkoin.Module().module))
    }

    val service: PriceService = get().get()
    val ipService: IPService by get().inject()

    runBlocking {
        println("BTC Price: ${service.getCurrentPrice("BTCEUR").price} EUR")
        println("Current IPv4: ${ipService.getIP()}")
        println("Current IPv6: ${
            kotlin.runCatching { ipService.getIP6() }.recover { if (it is SocketException) "Not supported" else throw it }.getOrThrow()
        }")
    }
}

@Module(includes = [KtorfitModule::class])
@ComponentScan("com.dshatz.ktorfitkoin")
class Module