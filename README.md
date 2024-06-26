# Generate a [Koin](https://github.com/InsertKoinIO/koin) module from your [Ktorfit](https://github.com/Foso/Ktorfit) services.

This ksp processor will find all Ktorfit services and create a Koin module with those.

## 1. Add KSP and Ktorfit-Koin
```kotlin
plugins {
  id("com.google.devtools.ksp") version "1.9.23-1.0.20"
}

dependencies {
  ksp("com.dshatz:ktorfit-koin:1.0.4-kotlin-1.9.23")
  implementation("com.dshatz:ktorfit-koin:1.0.4-kotlin-1.9.23")
  // or for kotlin-2.0-RC1
  ksp("com.dshatz:ktorfit-koin:1.0.4-kotlin-2.0-RC1")
  implementation("com.dshatz:ktorfit-koin:1.0.4-kotlin-2.0-RC1")

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
    return Ktorfit.Bulder().build()
  }

  @Module(include = [NetworkModule::class])
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

# Stability

This project is experimental and was made to address my usecase. 

I do aim to make it useful for others, so if your usecase does not work, create an issue with examples and I'll do my best.

Support for other kotlin, ksp and ktor versions will be added later.
