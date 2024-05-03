package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.binance.PriceService
import com.dshatz.ktorfitkoin.binance.ktorFitModule
import kotlinx.coroutines.test.runTest
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.verify.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class TestInject: KoinTest {

    private val priceService: PriceService by inject()

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun verifyModule() {
        Module().module.verify()
    }

    @Test
    fun testInject() = runTest {
        startKoin {
            modules(Module().module)
        }
        assertEquals("BTCEUR", priceService.getCurrentPrice("BTCEUR").symbol)
    }

}