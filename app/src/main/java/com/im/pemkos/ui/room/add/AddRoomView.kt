package com.im.pemkos.ui.room.add

import com.im.pemkos.model.Kost
import com.im.pemkos.model.Room


interface AddRoomView {
    fun showMessage(message: String)
    fun successAdd()
    fun showKost(rooms: List<Kost>)
}