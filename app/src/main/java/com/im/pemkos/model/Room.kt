package com.im.pemkos.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room (

        @SerializedName("id_kamar")
        val idRoom: String?=null,
        @SerializedName("id_kos")
        val idKost: String?=null,

        @SerializedName("nama_kos")
        val kostName: String?,
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
        val photo: String? = null,

        @SerializedName("status_kamar")
        val statusKamar: String,

        @SerializedName("id_anak_kos")
        val penghuni: String,

        @SerializedName("nama_anak_kos")
        val namapenghuni: String,
): Parcelable