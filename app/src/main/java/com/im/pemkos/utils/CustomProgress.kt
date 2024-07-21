package com.im.pemkos.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.im.pemkos.R

class CustomProgress(ctx: Context) : Dialog(ctx) {
    val cntxt: Context = ctx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(cntxt).inflate(R.layout.custom_progress, null, false))
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun showLoading(cancelable: Boolean = false) {
        try {
            this.setCancelable(cancelable)
            this.setCanceledOnTouchOutside(cancelable)
            this.show()
        } catch (e: Exception) {}
    }

    fun hideLoading() {
        try {
            this.dismiss()
        } catch (e: Exception) {}
    }
}