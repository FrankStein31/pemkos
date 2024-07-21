package com.im.pemkos.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class BillResponse (
    val status: Boolean,
    val message: String,
    val data: List<Bill>
)

data class BillUserResponse (
    val status: Boolean,
    val message: String,
    val data: List<BillUser>
)

data class PembayaranResponse (
    val metodePembayaran: String,
    val message: String,
    val data: List<Bill>
)

@Parcelize
data class Bill (
    @SerializedName("id_bill")
    val idBill: String,
    @SerializedName("metode_pembayaran")
    val metodePembayaran: String?,
    @SerializedName("id_anak_kos")
    val idTenant: String,
    @SerializedName("tgl_bill")
    val billDate: String,
    val nominal: String,
    val status: String,

    @SerializedName("id_kamar")
    val idRoom: String,
    @SerializedName("nama")
    val name: String,
    @SerializedName("kode")
    val code: String,
    val gender: String,
    @SerializedName("kontak")
    val contact: String,
    @SerializedName("alamat")
    val address: String,
    @SerializedName("tgl_masuk")
    val entryDate: String,
    @SerializedName("foto")
    val photo: String,


    @SerializedName("kode_kamar")
    val roomCode: String?,
    @SerializedName("tipe")
    val type: String?,
    @SerializedName("biaya")
    val price: String?,
    @SerializedName("fasilitas")
    val facilities: String?,
): Parcelable

@Parcelize
data class BillUser (
    @SerializedName("id_bill")
    val idBill: String,
    @SerializedName("metode_pembayaran")
    val metodePembayaran: String?,
    @SerializedName("id_anak_kos")
    val idTenant: String,
    @SerializedName("tgl_bill")
    val billDate: String,
    val nominal: String,
    val status: String,

    @SerializedName("id_kamar")
    val idRoom: String,
    @SerializedName("nama")
    val name: String,
    @SerializedName("kode")
    val code: String,
    val gender: String,
    @SerializedName("kontak")
    val contact: String,
    @SerializedName("alamat")
    val address: String,
    @SerializedName("tgl_masuk")
    val entryDate: String,
    @SerializedName("foto")
    val photo: String,


    @SerializedName("kode_kamar")
    val roomCode: String?,
    @SerializedName("tipe")
    val type: String?,
    @SerializedName("biaya")
    val price: String?,
    @SerializedName("fasilitas")
    val facilities: String?,
): Parcelable