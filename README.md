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

**Gradle:**

```kotlin

dependencies {
    // ...
    
    // Replace -SNAPSHOT with the build
    api("com.github.Cubxity", "AgonesKt", "-SNAPSHOT")

    // Choose your gRPC runtime
    runtimeOnly("io.grpc", "grpc-netty", "1.37.0")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    // ...
}
```