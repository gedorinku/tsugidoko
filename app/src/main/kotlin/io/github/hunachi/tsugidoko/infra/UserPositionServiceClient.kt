package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.UserPositionServiceGrpc
import gedorinku.tsugidoko_server.UserPositions
import io.github.hunachi.tsugidoko.util.NetworkState
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class UserPositionServiceClient : ServiceClient() {

    private var userPositionStub = UserPositionServiceGrpc.newBlockingStub(channel)

    override fun setUp(sessionId: String) {
        userPositionStub = MetadataUtils.attachHeaders(userPositionStub, setKeyMetadata(sessionId))
    }

    suspend fun userPosition() = coroutineScope {

        val createRequest = UserPositions.GetUserPositionRequest.newBuilder()
                .build()

        userPositionStub.getUserPosition(createRequest)
    }


    suspend fun sendUserPosition(bssId: String, isStayingNow: Boolean) = coroutineScope {

        try {
            val createRequest = UserPositions.UpdateUserPositionRequest.newBuilder()
                    .setBssid(bssId)
                    .setIsValid(isStayingNow)
                    .build()

            NetworkState.Success(userPositionStub.updateUserPosition(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<UserPositions.UserPosition>(e)
        }
    }
}