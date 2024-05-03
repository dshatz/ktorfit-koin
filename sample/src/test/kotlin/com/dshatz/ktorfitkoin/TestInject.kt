package com.dshatz.ktorfitkoin

import com.dshatz.ktorfitkoin.service.IPService
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.coroutines.test.runTest
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.verify.verify
import kotlin.test.Test
import kotlin.test.assertTrue

class TestInject: KoinTest {

    private val ipService: IPService by inject()

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun verifyModule() {
        Module().module.verify(
            extraTypes = listOf(HttpClientEngine::class, HttpClientConfig::class)
        )
    }

    @Test
    fun testInject() = runTest {
        startKoin {
            modules(Module().module)
        }
        assertTrue(ipService.getIP().matches(Regex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}\$")))
    }

}