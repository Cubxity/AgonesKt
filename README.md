# AgonesKt

Kotlin API wrapper for Google [Agones](https://agones.dev).

## Example
```kotlin
import dev.cubxity.libs.agones.AgonesSDK

suspend fun main() {
    AgonesSDK().use { sdk ->
        sdk.ready()    
        // ...
    }
}
```

## Getting Started
See [Agones Client SDK documentation](https://agones.dev/site/docs/guides/client-sdks/).