package com.im.pemkos.network.response


import com.im.pemkos.model.Tenant

data class AddTenantResponse (
    val success: Boolean,
    val message: String,
    val data: Tenant? = null
)

