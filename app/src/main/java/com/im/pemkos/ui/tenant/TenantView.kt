package com.im.pemkos.ui.tenant

import com.im.pemkos.model.Tenant

interface TenantView {
    fun showMessage(message: String)
    fun showAbk(listAbk: List<Tenant>)

}