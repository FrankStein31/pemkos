package com.im.pemkos.network.response


import com.im.pemkos.model.Kost

data class AddKostResponse (
    val success: Boolean,
    val message: String,
    val data: Kost? = null
)

