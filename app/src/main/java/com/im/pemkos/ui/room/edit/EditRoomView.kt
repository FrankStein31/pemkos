package com.im.pemkos.ui.room.edit

import com.im.pemkos.model.Kost


interface EditRoomView {
    fun showMessage(message: String)
    fun successEdit()
    fun showKost(rooms: List<Kost>)
}