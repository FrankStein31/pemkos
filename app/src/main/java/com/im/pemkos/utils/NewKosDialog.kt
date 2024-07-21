package com.im.pemkos.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.im.pemkos.R
import kotlinx.android.synthetic.main.new_kos_dialog.*

class NewKosDialog(ctx: Context, val onSubmit: (String) -> Unit ) : Dialog(ctx) {
    private val cntxt: Context = ctx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(cntxt).inflate(R.layout.new_kos_dialog, null, false))
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        tv_close.setOnClickListener {
            this.dismiss()
        }

        btn_submit.setOnClickListener {
            if(edt_name.text.toString().isEmpty()){
                Toast.makeText(cntxt, "Field nama tidak boleh kosong", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            onSubmit(edt_name.text.toString())
            this.dismiss()
        }
    }

}