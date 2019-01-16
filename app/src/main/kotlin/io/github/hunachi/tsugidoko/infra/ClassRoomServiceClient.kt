package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.ClassRoomServiceGrpc
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class ClassRoomServiceClient : ServiceClient() {

    private var classRoomStub = ClassRoomServiceGrpc.newBlockingStub(channel)

    suspend fun classRooms(sessionId: String, tags: List<TagOuterClass.Tag>) = coroutineScope {

        classRoomStub = MetadataUtils.attachHeaders(classRoomStub, setKeyMetadata(sessionId))

        val createRequest = ClassRooms.ListClassRoomsRequest.newBuilder()
                .apply { if(tags.isNotEmpty()) addAllTagIds(tags.map { it.id }) }
                .build()

        classRoomStub.listClassRooms(createRequest).classRoomsList
    }
}