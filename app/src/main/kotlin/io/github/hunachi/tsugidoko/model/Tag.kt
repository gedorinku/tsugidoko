package io.github.hunachi.tsugidoko.model

import gedorinku.tsugidoko_server.Tags

data class Tag(
        val id: Int = 0,
        val name: String,
        var isSelected: Boolean = false
) {
    fun convert(): Tags.Tag = Tags.Tag.newBuilder().also{
        it.id = this@Tag.id
        it.name = this@Tag.name
    }.build()
}

fun Tags.Tag.convertTag(isSelected: Boolean = false) = Tag(id, name, isSelected)
