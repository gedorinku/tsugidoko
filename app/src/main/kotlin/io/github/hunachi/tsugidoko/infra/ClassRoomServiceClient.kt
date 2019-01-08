package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.ClassRoomServiceGrpc
import gedorinku.tsugidoko_server.ClassRooms
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class ClassRoomServiceClient : ServiceClient() {

    private var classRoomStub = ClassRoomServiceGrpc.newBlockingStub(channel)

    suspend fun classRooms(sessionId: String) = coroutineScope {

        setKey(sessionId)

        val createRequest = ClassRooms.ListClassRoomsRequest.newBuilder()
                .build()

        classRoomStub.listClassRooms(createRequest).classRoomsList
    }

    override fun setKey(sessionId: String) {
        header.put(io.grpc.Metadata.Key.of("authorization", io.grpc.Metadata.ASCII_STRING_MARSHALLER), "session $sessionId")
        classRoomStub = MetadataUtils.attachHeaders(classRoomStub, header)
    }
}