package com.im.pemkos.ui.profil.ubahpassword

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.im.pemkos.R
import kotlinx.android.synthetic.main.activity_ubah_password.*

class UbahPasswordActivity : AppCompatActivity(), UbahPasswordView {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var presenter: UbahPasswordPresenter
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah_password)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        presenter =  UbahPasswordPresenter(this, this)

        id = sharedPref.getString("ID", "") ?: ""

        btn_submit.setOnClickListener {
            val pwd = edt_password.text.toString()
            val repwd = edt_repassword.text.toString()
            val currentPwd = edt_password_current.text.toString()
            if(currentPwd.isNullOrEmpty()){
                Toast.makeText(this, "Password saat ini harus diisi", Toast.LENGTH_LONG).show()
            } else if(pwd.isNullOrEmpty()){
                Toast.makeText(this, "Password baru harus diisi", Toast.LENGTH_LONG).show()
            } else if(repwd.isNullOrEmpty()){
                Toast.makeText(this, "Konfirmasi password baru harus diisi", Toast.LENGTH_LONG).show()
            } else if(pwd != repwd){
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_LONG).show()
            } else presenter.ubahPassword(oldPwd = currentPwd, newPwd = pwd, id = id)
        }

    }



    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun successUbahPassword() {
        showMessage("Ubah password berhasil")
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}