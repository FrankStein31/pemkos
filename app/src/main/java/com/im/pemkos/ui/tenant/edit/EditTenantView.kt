package com.im.pemkos.ui.tenant.edit

import com.im.pemkos.model.Room


interface EditTenantView {
    fun showMessage(message: String)
    fun successEdit()
    fun showRoom(rooms: List<Room>)
}