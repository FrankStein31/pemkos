package com.im.pemkos.network.request

import com.google.gson.annotations.SerializedName

data class AddTenantRequest (
        @SerializedName("id_anak_kos")
        val idTenant: String?=null,
        @SerializedName("id_kamar")
        val idRoom: String?,
        @SerializedName("nama")
        val name: String?,
        @SerializedName("kode")
        val code: String?,
        val gender: String?,
        @SerializedName("alamat")
        val address: String?,
        @SerializedName("kontak")
        val contact: String?,
        @SerializedName("tgl_masuk")
        val entryDate: String?,
        @SerializedName("foto")
        val photo: String? = null
)
