package com.im.pemkos.network.response

import com.im.pemkos.model.Room

data class RoomResponse (
        val status: Boolean,
        val message: String,
        val data: List<Room>
)


