package io.github.hunachi.tsugidoko.infra

import gedorinku.tsugidoko_server.TagServiceGrpc
import gedorinku.tsugidoko_server.Tags
import gedorinku.tsugidoko_server.UserTagServiceGrpc
import gedorinku.tsugidoko_server.UserTags
import io.github.hunachi.tsugidoko.model.Tag
import io.github.hunachi.tsugidoko.util.NetworkState
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.coroutineScope

class TagServiceClient : ServiceClient() {

    private var tagStub = TagServiceGrpc.newBlockingStub(channel)
    private var userTagStub = UserTagServiceGrpc.newBlockingStub(channel)

    override fun setUp(sessionId: String) {
        tagStub = MetadataUtils.attachHeaders(tagStub, setKeyMetadata(sessionId))
        userTagStub = MetadataUtils.attachHeaders(userTagStub, setKeyMetadata(sessionId))
    }

    suspend fun tags() = coroutineScope {

        try {
            val createRequest = Tags.ListTagsRequest.newBuilder()
                    .build()

            NetworkState.Success(tagStub.listTags(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Tags.ListTagsResponse>(e)
        }
    }

    suspend fun tag(tagId: Int) = coroutineScope {

        try {
            val createRequest = Tags.GetTagRequest.newBuilder()
                    .setTagId(tagId)
                    .build()

            NetworkState.Success(tagStub.getTag(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Tags.Tag>(e)
        }
    }

    suspend fun createTag(tag: Tag) = coroutineScope {
        try {
            val createRequest =
                    Tags.CreateTagRequest.newBuilder()
                            .setTag(tag.convert())
                            .build()

            NetworkState.Success(tagStub.createTag(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<Tags.Tag>(e)
        }
    }

    suspend fun updateTags(tags: List<Tag>) = coroutineScope {
        try {
            val createRequest =
                    UserTags.UpdateUserTagRequest.newBuilder()
                            .addAllTagIds(tags.map { it.id })
                            .build()

            NetworkState.Success(userTagStub.updateUserTag(createRequest))
        } catch (e: Exception) {
            NetworkState.Error<UserTags.UpdateUserTagResponse>(e)
        }
    }
}