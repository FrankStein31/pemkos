package com.im.pemkos.network.response

import com.google.gson.annotations.SerializedName

data class DeleteResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
