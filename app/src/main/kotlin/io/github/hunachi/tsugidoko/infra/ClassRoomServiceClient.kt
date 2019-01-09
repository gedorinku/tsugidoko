package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.ClassRoomServiceGrpc
import gedorinku.tsugidoko_server.ClassRooms
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class ClassRoomServiceClient : ServiceClient() {

    private var classRoomStub = ClassRoomServiceGrpc.newBlockingStub(channel)

    suspend fun classRooms(sessionId: String) = coroutineScope {

        classRoomStub = MetadataUtils.attachHeaders(classRoomStub, setKeyMetadata(sessionId))

        val createRequest = ClassRooms.ListClassRoomsRequest.newBuilder()
                .build()

        classRoomStub.listClassRooms(createRequest).classRoomsList
    }
}