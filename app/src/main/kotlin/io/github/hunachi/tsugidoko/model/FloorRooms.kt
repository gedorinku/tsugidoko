package io.github.hunachi.tsugidoko.model

import java.io.Serializable

data class FloorRooms(
        val buildingId: Int,
        val floor: Int,
        val rooms: List<ClassRoom>
) : Serializable