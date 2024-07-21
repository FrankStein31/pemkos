package com.im.pemkos.network.request

import com.google.gson.annotations.SerializedName

data class AddRoomRequest (

        @SerializedName("id_kamar")
        val idRoom: String?=null,
        @SerializedName("id_kos")
        val idKost: String?,
        @SerializedName("kode")
        val code: String?,
        @SerializedName("tipe")
        val type: String?,
        @SerializedName("biaya")
        val price: String?,
        val gender: String?,

        @SerializedName("fasilitas")
        val facilities: String?,

        @SerializedName("foto")
        val photo: String? = null
)
