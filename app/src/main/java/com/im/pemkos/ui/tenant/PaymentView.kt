package com.im.pemkos.ui.tenant

import com.im.pemkos.network.response.CheckoutResponse

interface PaymentView {
    fun showPaymentSuccess(response: CheckoutResponse, idBill: String)
    fun showMessagepayment(message: String)
}