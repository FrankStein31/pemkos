package com.im.pemkos.ui.bill

import com.im.pemkos.network.response.Bill
import com.im.pemkos.network.response.DataItem

interface BillByIdView {

    fun showMessageBill(message: String)
    fun showBillById(listAbk: List<DataItem?>?)
}
