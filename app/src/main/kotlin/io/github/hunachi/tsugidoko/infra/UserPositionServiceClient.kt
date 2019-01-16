package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.UserPositionServiceGrpc
import gedorinku.tsugidoko_server.UserPositions
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class UserPositionServiceClient : ServiceClient() {

    private var userPositionStub = UserPositionServiceGrpc.newBlockingStub(channel)

    suspend fun userPosition(sessionId: String, tags: List<TagOuterClass.Tag>) = coroutineScope {

        userPositionStub = MetadataUtils.attachHeaders(userPositionStub, setKeyMetadata(sessionId))

        val createRequest = UserPositions.GetUserPositionRequest.newBuilder()
                .build()

        userPositionStub.getUserPosition(createRequest)
    }


    suspend fun sendUserPosition(sessionId: String, bssId: String, isStayingNow: Boolean)= coroutineScope {

        userPositionStub = MetadataUtils.attachHeaders(userPositionStub, setKeyMetadata(sessionId))

        val createRequest = UserPositions.UpdateUserPositionRequest.newBuilder()
                .setBssid(bssId)
                .setIsValid(isStayingNow)
                .build()

        userPositionStub.updateUserPosition(createRequest)
    }
}