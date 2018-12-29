package io.github.hunachi.tsugidoko.model

data class ClassRoom(
        val id: Int,
        val name: String,
        val beacons: List<Beacon>,
        val latitude: Double,
        val longitude: Double,
        val buildingId: Int,
        val floor: Int
)