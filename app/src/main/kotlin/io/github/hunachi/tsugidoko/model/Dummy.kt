package io.github.hunachi.tsugidoko.model

import android.graphics.Point
import java.util.*

val dummyDetailMap = DetailMap(
        0,
        0,
        "sample",
        listOf(dummyFloorRooms(0), dummyFloorRooms(1), dummyFloorRooms(2), dummyFloorRooms(3))
)

fun dummyFloorRooms(num: Int) = FloorRooms(
        num,
        "https://gedorinku.github.io/tsugidoko-pic/1/1.png",
        listOf(dummyClassRoom(0), dummyClassRoom(1))
)

fun dummyClassRoom(num: Int) =
        ClassRoom(num,
                "classRoom + $num",
                listOf(/*Beacon(num, "$num", num)*/),
                -34.0 + Random().nextInt(10) % 10,
                151.0 + Random().nextInt(10) % 10,
                num,
                num,
                listOf()
                /*listOf(TagCount(Tag(((num + 2) % 2).toLong(), "dummy${((num + 2) % 2)}"), Random().nextInt(10)),
                        TagCount(Tag(((num + 1) % 2).toLong(), "dummy${((num + 1) % 2)}"), Random().nextInt(10)))*/,
                Point(0, 0))


val dummyTags = listOf(Tag(0, "dummy0"), Tag(1, "dummy1"), Tag(2, "dummy2"))

val dummyUser = User(0, "dummy", dummyTags)

val dummyClassRooms = listOf(dummyClassRoom(0), dummyClassRoom(1), dummyClassRoom(2))
