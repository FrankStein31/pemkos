package com.im.pemkos.ui.profil

import com.im.pemkos.network.response.Profil

interface ProfilView {
    fun showMessage(message: String)
    fun successUbahProfil(data: Profil?)
}