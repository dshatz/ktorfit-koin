package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.binance.IPService
import com.dshatz.ktorfitkoin.binance.KtorFitModule
import com.dshatz.ktorfitkoin.binance.PriceService
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

fun main() {
    startKoin {
        modules(com.dshatz.ktorfitkoin.Module().module)
    }

    val service: PriceService = get().get()
    val ipService: IPService by get().inject()

    runBlocking {
        println("BTC Price: ${service.getCurrentPrice("BTCEUR").price}")
        println("Current IP: ${ipService.getIP()}")
        println("Current IP6: ${ipService.getIP6()}")
    }
}

@Module(includes = [KtorFitModule::class])
@ComponentScan("com.dshatz.ktorfit")
class Module() {

}