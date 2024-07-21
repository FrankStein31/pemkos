package com.im.pemkos.network.response

import com.im.pemkos.model.Kost

data class KostResponse (
        val status: Boolean,
        val message: String,
        val data: List<Kost>
)


