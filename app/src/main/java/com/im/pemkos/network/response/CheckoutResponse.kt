package com.im.pemkos.network.response

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(

	@field:SerializedName("snap_token")
	val snapToken: String? = null,

	@field:SerializedName("additional_data")
	val additionalData: AdditionalData? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class AdditionalData(

	@field:SerializedName("customer_details")
	val customerDetails: CustomerDetails? = null,

	@field:SerializedName("transaction_details")
	val transactionDetails: TransactionDetails? = null
)

data class TransactionDetails(

	@field:SerializedName("gross_amount")
	val grossAmount: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null
)

data class CustomerDetails(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("tgl_masuk")
	val tglMasuk: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null
)
