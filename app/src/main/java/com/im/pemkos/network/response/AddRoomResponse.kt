package com.im.pemkos.network.response


import com.im.pemkos.model.Room

data class AddRoomResponse (
    val success: Boolean,
    val message: String,
    val data: Room? = null
)

