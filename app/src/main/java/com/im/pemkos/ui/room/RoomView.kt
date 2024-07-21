package com.im.pemkos.ui.room

import com.im.pemkos.model.Room

interface RoomView {
    fun showMessage(message: String)
    fun showRoom(rooms: List<Room>)
}