package com.im.pemkos.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import com.im.pemkos.R
import kotlinx.android.synthetic.main.activity_add_tenant.sp_room
import kotlinx.android.synthetic.main.new_bill_dialog.btn_submit
import kotlinx.android.synthetic.main.new_bill_dialog.sp_month
import kotlinx.android.synthetic.main.new_bill_dialog.sp_year
import kotlinx.android.synthetic.main.new_bill_dialog.tv_close
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class NewBillDialog(ctx: Context, val onSubmit: (Int, Int) -> Unit ) : Dialog(ctx) {
    private val cntxt: Context = ctx
    var months: List<Int> = mutableListOf(1,2,3,4,5,6,7,8,9,10,11,12)
    var years: List<Int> = mutableListOf(2024, 2023, 2022, 2021, 2020)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(cntxt).inflate(R.layout.new_bill_dialog, null, false))
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        tv_close.setOnClickListener {
            this.dismiss()
        }

        val adapterMonth = ArrayAdapter(cntxt,
            android.R.layout.simple_spinner_dropdown_item, months)
        val adapterYear = ArrayAdapter(cntxt,
            android.R.layout.simple_spinner_dropdown_item, years)
        sp_month.adapter = adapterMonth
        sp_year.adapter = adapterYear
        btn_submit.setOnClickListener {
            var monthNow: Int = SimpleDateFormat("MM").format(Date()).toInt()
            var yearNow: Int = SimpleDateFormat("yyyy").format(Date()).toInt()
            var month: Int = sp_month.selectedItem as Int
            var year: Int = sp_year.selectedItem as Int
            if(month>monthNow && year>=yearNow){
                Toast.makeText(cntxt, "Tagihan belum bisa dibuat", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            Log.d("Mnmdbjkbds", " ${month>monthNow} ${year>=yearNow} $month $monthNow $year $yearNow")
            onSubmit(month, year)
            this.dismiss()
        }
    }

}