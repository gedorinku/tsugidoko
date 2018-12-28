package io.github.hunachi.tsugidoko.model

import android.graphics.Point

val dammyDetailMap = DetailMap(
        0,
        "sample",
        "https://avatars0.githubusercontent.com/u/16878520?s=400&u=ca6ec3a48a4dd795a8783c432d4f2ee02fa531d6&v=4",
        listOf(dummyFloorRooms(0), dummyFloorRooms(1), dummyFloorRooms(2), dummyFloorRooms(3))
)

fun dummyFloorRooms(num: Int) = FloorRooms(
        num,
        num,
        listOf(dummyClassRoom(0), dummyClassRoom(1))
)

fun dummyClassRoom(num: Int) = ClassRoom(
        num,
        "classRoom + $num",
        listOf(Beacon(num, "$num", num)),
        num.toDouble(),
        num.toDouble(),
        num,
        Point(0, 0)
)