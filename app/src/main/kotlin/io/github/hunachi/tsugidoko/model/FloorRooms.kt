package io.github.hunachi.tsugidoko.model

import gedorinku.tsugidoko_server.ClassRooms
import java.io.Serializable

data class FloorRooms(
        val floor: Int,
        val imageUrl: String,
        val rooms: List<ClassRooms.ClassRoom>
) : Serializable