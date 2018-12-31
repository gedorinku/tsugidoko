package io.github.hunachi.tsugidoko.infra

/*import gedorinku.tsugidoko_server.SessionServiceGrpc
import gedorinku.tsugidoko_server.Sessions
import kotlinx.coroutines.coroutineScope*/

class SessionServiceClient : ServiceClient() {

    /*private val sessionStub = SessionServiceGrpc.newBlockingStub(channel)


    suspend fun createSession(userName: String, password: String) = coroutineScope {

        val createRequest = Sessions.CreateSessionRequest.newBuilder()
                .setName(userName)
                .setPassword(password)
                .build()
        sessionStub.createSession(createRequest)
    }

    override fun setKey(sessionId: String) {}*/
}