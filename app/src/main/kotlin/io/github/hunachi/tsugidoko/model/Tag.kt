package io.github.hunachi.tsugidoko.model

import gedorinku.tsugidoko_server.Tags

data class Tag(
        val id: Int,
        val name: String,
        var isSelected: Boolean = false
) {
    fun convert(): Tags.Tag = Tags.Tag.newBuilder().apply {
        id = id
        name = name
    }.build()
}

fun Tags.Tag.convertTag() = Tag(id, name)