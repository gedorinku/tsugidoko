package io.github.hunachi.tsugidoko.model

import java.io.Serializable

data class Building(
        val id: Int,
        val name: String,
        val rooms: List<FloorRooms>
) : Serializable