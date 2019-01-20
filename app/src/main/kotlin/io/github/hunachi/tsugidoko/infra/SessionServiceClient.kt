package io.github.hunachi.tsugidoko.infra

import android.content.SharedPreferences
import gedorinku.tsugidoko_server.SessionServiceGrpc
import gedorinku.tsugidoko_server.Sessions
import gedorinku.tsugidoko_server.Users
import io.github.hunachi.tsugidoko.util.NetworkState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SessionServiceClient(
        private val userServiceClient: UserServiceClient,
        preferences: SharedPreferences
) : ServiceClient(preferences) {

    private val sessionStub = SessionServiceGrpc.newBlockingStub(channel)

    suspend fun createSession(userName: String, password: String) = coroutineScope {
        try {
            val createRequest = Sessions.CreateSessionRequest.newBuilder()
                    .setName(userName)
                    .setPassword(password)
                    .build()

            NetworkState.Success(sessionStub.createSession(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Sessions.Session>(e)
        }
    }

    suspend fun firstCreateSession(userName: String, password: String) = coroutineScope {
        try {
            val user = async { userServiceClient.createUser(userName, password) }
            user.await().let {
                return@let when (it) {
                    is NetworkState.Success -> {
                        val createRequest = Sessions.CreateSessionRequest.newBuilder()
                                .setName(it.result.name)
                                .setPassword(password)
                                .build()

                        NetworkState.Success(sessionStub.createSession(createRequest))
                    }
                    is NetworkState.Error -> NetworkState.Error<Sessions.Session>(it.e)
                }
            }
        } catch (e: Exception) {
            NetworkState.Error<Sessions.Session>(e)
        }
    }
}