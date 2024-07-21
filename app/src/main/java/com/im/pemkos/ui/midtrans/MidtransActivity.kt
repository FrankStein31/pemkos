package com.im.pemkos.ui.midtrans

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.im.pemkos.R
import com.im.pemkos.ui.MainActivity
import com.im.pemkos.ui.TransaksiBerhasilActivity

class MidtransActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_midtrans)

        val billId = intent.getStringExtra("IDBill")

        val webView = findViewById<WebView>(R.id.webview)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.e("klikitem", "Loading URL: $url")
                if (url != null) {
                    if (url.contains("Payment successful") || url.contains("completed")) {
                        val intent = Intent(this@MidtransActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        return true
                    }
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.e("klikitem", "Finished loading URL: $url")
                if(url == "http://example.com/?order_id=${billId}&status_code=200&transaction_status=settlement"){
                    val intent = Intent(this@MidtransActivity, TransaksiBerhasilActivity::class.java)
                    intent.putExtra("IDBill", billId)
                    intent.putExtra("METODEPEMBAYARAN", "online")
                    startActivity(intent)

                }
            }
        }
        webView.settings.javaScriptEnabled = true
        val url = intent.getStringExtra("URL")
        Log.e("klikitem", "Loading URL BOS: $url")
        if (url != null) {
            webView.loadUrl(url)
        } else {
            Log.e("klikitem", "URL is null")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}