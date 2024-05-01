# Generate a [Koin](https://github.com/InsertKoinIO/koin) module from your [Ktorfit](https://github.com/Foso/Ktorfit) services.

This ksp processor will find all Ktorfit services and create a Koin module with those.

## Add KSP and Ktorfit-Koin
```kotlin
plugins {

dependencies {
  ksp("com.dshatz:ktorfit-koin:1.0.0")
}
```
