package com.im.pemkos.network.request

import com.google.gson.annotations.SerializedName

data class AddKostRequest (

        @SerializedName("id_kos")
        val idKost: String?=null,
        @SerializedName("nama")
        val name: String?,
)
