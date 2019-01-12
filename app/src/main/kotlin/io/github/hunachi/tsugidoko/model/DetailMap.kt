package io.github.hunachi.tsugidoko.model

import java.io.Serializable

data class DetailMap(
        val id: Int,
        val buildingId: Int,
        val name: String,
        val rooms: List<FloorRooms>
) : Serializable