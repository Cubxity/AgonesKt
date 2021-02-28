package dev.cubxity.libs.agones

import agones.dev.sdk.Sdk
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.Closeable
import java.time.Duration
import java.util.concurrent.TimeUnit
import agones.dev.sdk.SDKGrpcKt as AgonesSDKGrpc
import agones.dev.sdk.alpha.Alpha as AlphaSdk
import agones.dev.sdk.beta.SDKGrpcKt as AgonesBetaSDKGrpc
import agones.dev.sdk.alpha.SDKGrpcKt as AgonesAlphaSDKGrpc

/**
 * Kotlin wrapper for Agones' GRPC SDK. This class' constructor is blocking.
 *
 * @param host Agones GRPC service's host
 * @param port Agones GRPC service's port
 * @return an instance of [AgonesSDK]
 */
class AgonesSDK(host: String, port: Int) : Closeable {
    private val channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build()

    private val stub = AgonesSDKGrpc.SDKCoroutineStub(channel)

    val beta = Beta(channel)
    val alpha = Alpha(channel)

    suspend fun ready() {
        stub.ready(Sdk.Empty.getDefaultInstance())
    }

    suspend fun allocate() {
        stub.allocate(Sdk.Empty.getDefaultInstance())
    }

    suspend fun shutdown() {
        stub.shutdown(Sdk.Empty.getDefaultInstance())
    }

    suspend fun health(stream: Flow<Unit>) {
        stub.health(stream.map { Sdk.Empty.getDefaultInstance() })
    }

    suspend fun getGameServer(): Sdk.GameServer =
        stub.getGameServer(Sdk.Empty.getDefaultInstance())

    fun watchGameServer(): Flow<Sdk.GameServer> =
        stub.watchGameServer(Sdk.Empty.getDefaultInstance())

    suspend fun setLabel(key: String, value: String) {
        stub.setLabel(
            Sdk.KeyValue.newBuilder()
                .setKey(key)
                .setValue(value)
                .build()
        )
    }

    suspend fun setAnnotation(key: String, value: String) {
        stub.setAnnotation(
            Sdk.KeyValue.newBuilder()
                .setKey(key)
                .setValue(value)
                .build()
        )
    }

    suspend fun reserve(duration: Duration) {
        stub.reserve(
            Sdk.Duration.newBuilder()
                .setSeconds(duration.seconds)
                .build()
        )
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    class Beta internal constructor(channel: Channel) {
        private val stub = AgonesBetaSDKGrpc.SDKCoroutineStub(channel)
    }

    class Alpha internal constructor(channel: Channel) {
        private val stub = AgonesAlphaSDKGrpc.SDKCoroutineStub(channel)

        suspend fun playerConnect(playerID: String) {
            stub.playerConnect(
                AlphaSdk.PlayerID.newBuilder()
                    .setPlayerID(playerID)
                    .build()
            )
        }

        suspend fun playerDisconnect(playerID: String) {
            stub.playerDisconnect(
                AlphaSdk.PlayerID.newBuilder()
                    .setPlayerID(playerID)
                    .build()
            )
        }

        suspend fun setPlayerCapacity(count: Long) {
            stub.setPlayerCapacity(
                AlphaSdk.Count.newBuilder()
                    .setCount(count)
                    .build()
            )
        }

        suspend fun getPlayerCapacity(): Long =
            stub.getPlayerCapacity(AlphaSdk.Empty.getDefaultInstance()).count

        suspend fun getPlayerCount(): Long =
            stub.getPlayerCount(AlphaSdk.Empty.getDefaultInstance()).count

        suspend fun isPlayerConnected(playerID: String): Boolean {
            return stub.isPlayerConnected(
                AlphaSdk.PlayerID.newBuilder()
                    .setPlayerID(playerID)
                    .build()
            ).bool
        }

        suspend fun getConnectedPlayers(): List<String> =
            stub.getConnectedPlayers(AlphaSdk.Empty.getDefaultInstance()).listList
    }
}

/**
 * Constructs a new instance of [AgonesSDK] with default settings. This method is blocking.
 *
 * @return an instance of [AgonesSDK]
 */
fun AgonesSDK(): AgonesSDK {
    val port = System.getenv("AGONES_SDK_GRPC_PORT")?.toIntOrNull() ?: 9357
    return AgonesSDK("localhost", port)
}
