package com.im.pemkos.network.response


import com.im.pemkos.model.Tenant

data class TenantResponse (
    val status: Boolean,
    val message: String,
    val data: List<Tenant>
)

