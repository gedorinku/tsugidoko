package io.github.hunachi.tsugidoko.model

import gedorinku.tsugidoko_server.Tags
import io.github.hunachi.tsugidoko.R

data class Tag(
        val id: Int = 0,
        val name: String,
        var isSelected: Boolean = false
) {
    fun convert(): Tags.Tag = Tags.Tag.newBuilder().also {
        it.id = this@Tag.id
        it.name = this@Tag.name
    }.build()
}

fun Tags.Tag.convertTag(isSelected: Boolean = false) = Tag(id, name, isSelected)

enum class TagCountLevel(val markerIcon: Int) {
    MORE_THAN_0(R.drawable.arrow_blue),
    MORE_THAN_5(R.drawable.arrow_yellow),
    MORE_THAN_10(R.drawable.arrow_red),
    MANY(R.drawable.arrow_red)
}

fun Int.tagCountLevel() = when (this) {
    in 0..4 -> TagCountLevel.MORE_THAN_0
    in 5..10 -> TagCountLevel.MORE_THAN_5
    in 10..20 -> TagCountLevel.MORE_THAN_10
    else -> TagCountLevel.MANY
}
