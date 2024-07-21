package com.im.pemkos.ui.tenant.add

import com.im.pemkos.model.Room


interface AddTenantView {
    fun showMessage(message: String)
    fun successAdd()
    fun showRoom(rooms: List<Room>)
}