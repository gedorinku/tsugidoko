package io.github.hunachi.tsugidoko.infra

import android.content.SharedPreferences
import gedorinku.tsugidoko_server.ClassRoomServiceGrpc
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.type.TagOuterClass
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class ClassRoomServiceClient(preferences: SharedPreferences) : ServiceClient(preferences) {

    private var classRoomStub = ClassRoomServiceGrpc.newBlockingStub(channel)

    init {
        classRoomStub = MetadataUtils.attachHeaders(classRoomStub, setKeyMetadata())
    }


    suspend fun classRooms(tags: List<TagOuterClass.Tag>) = coroutineScope {

        val createRequest = ClassRooms.ListClassRoomsRequest.newBuilder()
                .apply { if (tags.isNotEmpty()) addAllTagIds(tags.map { it.id }) }
                .build()

        classRoomStub.listClassRooms(createRequest).classRoomsList
    }
}