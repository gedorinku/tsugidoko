package io.github.hunachi.tsugidoko.model

import java.io.Serializable

data class FloorRooms(
        val floor: Int,
        val imageUrl: String,
        val rooms: List<ClassRoom>
) : Serializable