# Generate a [Koin](https://github.com/InsertKoinIO/koin) module from your [Ktorfit](https://github.com/Foso/Ktorfit) services.

This ksp processor will find all Ktorfit services and create a Koin module with those.
Compiled with Kotlin 2.0.21. Please report compatibility with earlier/later versions.

## 1. Add KSP and Ktorfit-Koin
```kotlin
plugins {
  // Add ksp
}

dependencies {
  ksp("com.dshatz:ktorfit-koin:1.1.0")
  implementation("com.dshatz:ktorfit-koin-lib:1.1.0")
  // Also add ktorfit and koin
}
```

## 2. Define your ktorfit service and Ktorfit module
```kotlin
interface PriceService {
    @GET("https://api.binance.com/api/v3/ticker/price")
    suspend fun getCurrentPrice(@Query("symbol") symbol: String): Response
}

@ServiceModule(packageScan = "com.example")
class NetworkModule
```

## 3. Start koin
```kotlin
  val module = module {
    single<Ktorfit> { Ktorfit.Builder().build() } 
  }
  startKoin {
        // networkModule is the Koin module generated by Ktorfit-Koin.
        modules(module, networkModule)
    }
```
### 3.1 Or with Koin annotations
```kotlin
  @Single
  fun provideKtorfit(): Ktorfit {
    return Ktorfit.Builder().build()
  }

  @Module(includes = [NetworkModule::class])
  @ComponentScan(...)
  class MyModule

  startKoin {
        modules(MyModule().module)
    }
```

## 4. Inject PriceService
```kotlin
@Single
class PriceRepo(private val priceService: PriceService) {}
```

or 

```kotlin
val service: PriceService by get().inject()
```

# Compile-time safety
This library supports KOIN_CONFIG_CHECK.

# Stability

This project is experimental and was made to address my usecase. 

I do aim to make it useful for others, so if your usecase does not work, create an issue with examples, and I'll do my best.
