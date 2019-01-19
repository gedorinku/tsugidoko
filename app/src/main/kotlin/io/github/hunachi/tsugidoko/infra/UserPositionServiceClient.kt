package io.github.hunachi.tsugidoko.infra

import android.content.SharedPreferences
import gedorinku.tsugidoko_server.UserPositionServiceGrpc
import gedorinku.tsugidoko_server.UserPositions
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.github.hunachi.tsugidoko.util.NetworkState
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class UserPositionServiceClient(preferences: SharedPreferences) : ServiceClient(preferences) {

    private var userPositionStub = UserPositionServiceGrpc.newBlockingStub(channel)

    init {
        userPositionStub = MetadataUtils.attachHeaders(userPositionStub, setKeyMetadata())
    }

    suspend fun userPosition() = coroutineScope {

        val createRequest = UserPositions.GetUserPositionRequest.newBuilder()
                .build()

        userPositionStub.getUserPosition(createRequest)
    }


    suspend fun sendUserPosition(bssId: String, isStayingNow: Boolean): NetworkState<UserPositions.UserPosition> = coroutineScope {

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