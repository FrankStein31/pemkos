package com.im.pemkos.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kost (

        @SerializedName("id_kos")
        val idKost: String?=null,

        @SerializedName("nama_kos")
        val name: String?,

): Parcelable