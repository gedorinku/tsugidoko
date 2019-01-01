package io.github.hunachi.tsugidoko.model

data class User (
    val id: Int,
    val name: String,
    val tags: List<Tag>
)
