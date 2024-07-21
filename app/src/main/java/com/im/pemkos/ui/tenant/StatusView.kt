package com.im.pemkos.ui.tenant

import com.im.pemkos.network.response.CheckoutResponse
import com.im.pemkos.network.response.StatusResponse

interface StatusView {
    fun showPaymentSuccess(response: StatusResponse)
    fun showMessage(message: String)
}