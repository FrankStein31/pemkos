package com.im.pemkos.network.response


data class LoginResponse (
        val success: Boolean,
        val message: String,
        val data: LoginData? = null
)


data class LoginData (
        val id: String,
        val role: String,
)