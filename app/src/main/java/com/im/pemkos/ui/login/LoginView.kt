package com.im.pemkos.ui.login

import com.im.pemkos.network.response.LoginData

interface LoginView {
    fun setData(data: LoginData)
    fun showMessage(message: String)
}