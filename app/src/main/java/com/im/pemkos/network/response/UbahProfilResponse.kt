package com.im.pemkos.network.response

import com.google.gson.annotations.SerializedName

data class UbahProfilResponse (
        val success: Boolean,
        val message: String,
        val data: Profil? = null
)

data class Profil (
        @SerializedName("nama_sekolah")
        val namaSekolah: String,

        @SerializedName("nama_kepala_Sekolah")
        val namaKepalaSekolah: String,

        val alamat: String,
        val email: String,
        val foto: String
)
