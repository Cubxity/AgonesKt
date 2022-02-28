import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"
    maven
}

group = "dev.cubxity.libs"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(kotlin("bom")))
    api(platform("io.grpc:grpc-bom:1.44.1"))

    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    api("io.grpc:grpc-protobuf")
    api("io.grpc:grpc-kotlin-stub:1.2.1")
    api("com.google.protobuf:protobuf-java-util:3.19.4")
    api("com.google.protobuf:protobuf-kotlin:3.19.4")

    runtimeOnly("io.grpc:grpc-netty:1.44.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.44.1"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.2.1:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("kotlin")
            }
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs(
                file("$buildDir/generated/source/proto/main/java"),
                file("$buildDir/generated/source/proto/main/grpc"),
                file("$buildDir/generated/source/proto/main/grpckt")
            )
        }
        resources {
            srcDir("src/main/proto")
        }
    }
}
