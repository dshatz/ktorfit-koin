package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.binance.PriceService
import com.dshatz.ktorfitkoin.binance.ktorFitModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

fun main() {
    startKoin {
        defaultModule()
        modules(defaultModule, ktorFitModule)
    }

    val service: PriceService = get().get()

    runBlocking {
        println(service.getCurrentPrice("BTCEUR").price)
    }
}