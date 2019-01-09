package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.UserServiceGrpc
import gedorinku.tsugidoko_server.Users
import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class UserServiceClient : ServiceClient() {

    private var userStub = UserServiceGrpc.newBlockingStub(channel)


    suspend fun user(sessionId: String) = coroutineScope {

        userStub = MetadataUtils.attachHeaders(userStub, setKeyMetadata(sessionId))

        val createRequest = Users.GetCurrentUserRequest.newBuilder()
                .build()
        userStub.getCurrentUser(createRequest)
    }


    suspend fun createUser(userName: String, password: String) = coroutineScope {

        val createRequest = Users.CreateUserRequest.newBuilder()
                .setName(userName)
                .setPassword(password)
                .build()

        userStub.createUser(createRequest)
    }
}