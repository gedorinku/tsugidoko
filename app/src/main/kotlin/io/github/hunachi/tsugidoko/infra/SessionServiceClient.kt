package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.SessionServiceGrpc
import gedorinku.tsugidoko_server.Sessions
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SessionServiceClient(private val userServiceClient: UserServiceClient) : ServiceClient() {

    private val sessionStub = SessionServiceGrpc.newBlockingStub(channel)


    suspend fun createSession(userName: String, password: String) = coroutineScope {

        val createRequest = Sessions.CreateSessionRequest.newBuilder()
                .setName(userName)
                .setPassword(password)
                .build()
        sessionStub.createSession(createRequest)
    }

    suspend fun firstCreateSession(userName: String, password: String): Sessions.Session = coroutineScope {

        val user = async {
            userServiceClient.createUser(userName, password)
        }

        val name = user.await().name

        val createRequest = Sessions.CreateSessionRequest.newBuilder()
                .setName(name)
                .setPassword(password)
                .build()
        sessionStub.createSession(createRequest)
    }
}