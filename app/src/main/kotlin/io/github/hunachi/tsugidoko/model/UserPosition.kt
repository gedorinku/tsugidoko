package io.github.hunachi.tsugidoko.model

data class UserPosition(
    val userId: Int,
    val classRoomId: Int,
    val isValid: Boolean
)