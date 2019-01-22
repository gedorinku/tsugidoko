package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.UserServiceGrpc
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.util.NetworkState
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class UserServiceClient : ServiceClient() {

    private var userStub = UserServiceGrpc.newBlockingStub(channel)

    override fun setUp(sessionId: String) {
        userStub = MetadataUtils.attachHeaders(userStub, setKeyMetadata(sessionId))
    }

    suspend fun user() = coroutineScope {
        try {
            val createRequest = Users.GetCurrentUserRequest.newBuilder()
                    .build()

            NetworkState.Success(userStub.getCurrentUser(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Users.User>(e)
        }
    }


    suspend fun createUser(userName: String, password: String) = coroutineScope {
        try {
            val createRequest = Users.CreateUserRequest.newBuilder()
                    .setName(userName)
                    .setPassword(password)
                    .build()

            NetworkState.Success(userStub.createUser(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Users.User>(e)
        }
    }
}