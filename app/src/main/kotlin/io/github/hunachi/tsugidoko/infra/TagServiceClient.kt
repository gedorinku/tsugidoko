package io.github.hunachi.tsugidoko.infra

class TagServiceClient : ServiceClient() {

  /*  private var tagStub = UserServiceGrpc.newBlockingStub(channel)

    suspend fun user(sessionId: String) = coroutineScope {

        tagStub = MetadataUtils.attachHeaders(tagStub, setKeyMetadata(sessionId))

        val createRequest = Users.GetCurrentUserRequest.newBuilder()
                .build()

        tagStub.getCurrentUser(createRequest)
    }


    suspend fun createUser(userName: String, password: String) = coroutineScope {

        val createRequest = Users.CreateUserRequest.newBuilder()
                .setName(userName)
                .setPassword(password)
                .build()

        tagStub.createUser(createRequest)
    }*/
}