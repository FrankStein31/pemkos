package com.im.pemkos.ui.kost

import com.im.pemkos.model.Kost


interface KostView {
    fun showMessage(message: String)
    fun successAdd()
    fun successDelete()
    fun showKost(list: List<Kost>)
}