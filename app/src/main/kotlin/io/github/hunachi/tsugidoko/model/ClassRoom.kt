package io.github.hunachi.tsugidoko.model

import android.graphics.Point

data class ClassRoom(
        val id: Int,
        val name: String,
        val beacons: List<Beacon>,
        val latitude: Double,
        val longitude: Double,
        val buildingId: Int,
        val floor: Int,
        val tagCounts: List<TagCount>,
        val detailPosition: Point
)