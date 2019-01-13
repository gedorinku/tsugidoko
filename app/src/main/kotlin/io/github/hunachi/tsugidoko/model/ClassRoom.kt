package io.github.hunachi.tsugidoko.model

import android.graphics.PointF
import gedorinku.tsugidoko_server.ClassRooms
import gedorinku.tsugidoko_server.type.BeaconOuterClass
import gedorinku.tsugidoko_server.type.TagCountOuterClass

data class ClassRoom(
        val id: Int,
        val name: String,
        val beacons: List<BeaconOuterClass.Beacon>,
        val latitude: Double,
        val longitude: Double,
        val buildingId: Int,
        val floor: Int,
        val tagCounts: List<TagCountOuterClass.TagCount>,
        val detailPosition: PointF
)

fun ClassRooms.ClassRoom.convertToClassRoom() = ClassRoom(
        classRoomId,
        name,
        beaconsList,
        latitude,
        longitude,
        buildingId,
        floor,
        tagCountsList,
        PointF(localX.toFloat(), localY.toFloat())
)