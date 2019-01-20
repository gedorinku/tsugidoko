package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.ClassRoomServiceGrpc
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.Tags
import io.github.hunachi.tsugidoko.util.NetworkState
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class ClassRoomServiceClient : ServiceClient() {

    private var classRoomStub = ClassRoomServiceGrpc.newBlockingStub(channel)

    override fun setUp(sessionId: String) {
        classRoomStub = MetadataUtils.attachHeaders(classRoomStub, setKeyMetadata(sessionId))
    }

    suspend fun classRooms(tags: List<Tags.Tag>) = coroutineScope {
        try {
            val createRequest = ClassRooms.ListClassRoomsRequest.newBuilder()
                    .apply { if (tags.isNotEmpty()) addAllTagIds(tags.map { it.id }) }
                    .build()

            NetworkState.Success(classRoomStub.listClassRooms(createRequest).classRoomsList)
        } catch (e: Exception) {
            NetworkState.Error<List<ClassRooms.ClassRoom>>(e)
        }
    }
}