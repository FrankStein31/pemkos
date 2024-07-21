package com.im.pemkos.ui.bill

import com.im.pemkos.model.Tenant
import com.im.pemkos.network.response.Bill
import com.im.pemkos.network.response.BillUser

interface BillView {
    fun showMessage(message: String)
    fun showBill(listAbk: List<Bill>)
//    fun showBillUser(billUsers: List<BillUser>)
}
