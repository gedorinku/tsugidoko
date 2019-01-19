package io.github.hunachi.tsugidoko.model

import gedorinku.tsugidoko_server.type.TagOuterClass

data class Tag(
        val id: Int,
        val name: String,
        var isSelected: Boolean = false
) {
    fun convert(): TagOuterClass.Tag = TagOuterClass.Tag.newBuilder().apply {
        id = id
        name = name
    }.build()
}

fun TagOuterClass.Tag.convertTag() = Tag(id, name)