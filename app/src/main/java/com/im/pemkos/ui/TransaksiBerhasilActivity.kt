package com.im.pemkos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.im.pemkos.R
import com.im.pemkos.network.response.StatusResponse
import com.im.pemkos.ui.tenant.StatusPresenter
import com.im.pemkos.ui.tenant.StatusView

class TransaksiBerhasilActivity : AppCompatActivity(), StatusView {

    private lateinit var statusPresenter: StatusPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_berhasil)

        statusPresenter = StatusPresenter(this, this)


        val metodePembayaran = intent.getStringExtra("METODEPEMBAYARAN")
        val billId = intent.getStringExtra("IDBill")
        if(billId != null && metodePembayaran != null){
            statusPresenter.setStatusPayment(billId, metodePembayaran)
        }

        val buttonHome = findViewById<Button>(R.id.btnBackToHome)

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun showPaymentSuccess(response: StatusResponse) {
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}