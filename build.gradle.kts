import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf

plugins {
    kotlin("jvm") version "1.4.31"
    id("com.google.protobuf") version "0.8.15"
}

group = "dev.cubxity.libs"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    api("com.google.protobuf:protobuf-java-util:3.15.3")
    api("io.grpc:grpc-kotlin-stub:1.0.0")

    runtimeOnly("io.grpc:grpc-netty:1.36.0")
}

protobuf {
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.36.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
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
