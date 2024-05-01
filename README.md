# Generate a [Koin](https://github.com/InsertKoinIO/koin) module from your [Ktorfit](https://github.com/Foso/Ktorfit) services.

This ksp processor will find all Ktorfit services and create a Koin module with those.

## Add KSP and Ktorfit-Koin
```kotlin
plugins {
  id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

dependencies {
  ksp("com.dshatz:ktorfit-koin:1.0.0")
  // Also add ktorfit and koin
}
```

## Define your ktorfit service
```kotlin
interface PriceService {
    @GET("https://api.binance.com/api/v3/ticker/price")
    suspend fun getCurrentPrice(@Query("symbol") symbol: String): Response
}
```

## Start koin
```kotlin
  startKoin {
        // Default module should provide a Ktorfit instance!
        // ktorFitModule is the Koin module generated by this Ktorfit-Koin.
        modules(defaultModule, ktorFitModule)
    }
```

## Inject PriceService normally
```kotlin
@Single
class PriceRepo(private val priceService: PriceService) {}
```

or 

```kotlin
val service: PriceService by get().inject()
```

# Stability

This project is experimental and was made to address my usecase. 

I do aim to make it useful for others, so if your usecase does not work, create an issue with examples and I'll do my best.

Support for other kotlin, ksp and ktor versions will be added later.
