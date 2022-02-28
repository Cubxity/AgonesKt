import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"
    maven
}

group = "dev.cubxity.libs"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    platform(kotlin("bom"))
    api(kotlin("stdlib"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    api("com.google.protobuf:protobuf-java-util:3.19.4")
    api("io.grpc:grpc-kotlin-stub:1.2.1")

    runtimeOnly("io.grpc:grpc-netty:1.44.1")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.44.1"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.2.1:jdk7@jar"
        }
    }
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
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
