package com.im.pemkos.network.response

import com.google.gson.annotations.SerializedName

data class BillByIdResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItem(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("tgl_masuk")
	val tglMasuk: String? = null,

	@field:SerializedName("fasilitas")
	val fasilitas: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("id_bill")
	val idBill: String? = null,

	@field:SerializedName("metode_pembayaran")
	val metodePembayaran: Any? = null,

	@field:SerializedName("id_kos")
	val idKos: String? = null,

	@field:SerializedName("kontak")
	val kontak: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("id_anak_kos")
	val idAnakKos: String? = null,

	@field:SerializedName("tgl_bill")
	val tglBill: String? = null,

	@field:SerializedName("status_kamar")
	val statusKamar: String? = null,

	@field:SerializedName("nominal")
	val nominal: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("biaya")
	val biaya: String? = null,

	@field:SerializedName("kode")
	val kode: String? = null,

	@field:SerializedName("kode_kamar")
	val kodeKamar: String? = null,

	@field:SerializedName("id_kamar")
	val idKamar: String? = null,

	@field:SerializedName("tipe")
	val tipe: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
