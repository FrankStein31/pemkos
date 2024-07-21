package com.im.pemkos.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tenant (
    @SerializedName("id_anak_kos")
    val idTenant: String,
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
    val photo: String? = null,


    @SerializedName("kode_kamar")
    val roomCode: String?,
    @SerializedName("tipe")
    val type: String?,
    @SerializedName("biaya")
    val price: String?,
    @SerializedName("fasilitas")
    val facilities: String?,
    @SerializedName("foto_kamar")
    val roomPhoto: String? = null

): Parcelable