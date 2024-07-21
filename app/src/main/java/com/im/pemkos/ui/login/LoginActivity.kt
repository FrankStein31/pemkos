package com.im.pemkos.ui.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.im.pemkos.ui.MainActivity
import com.im.pemkos.R
import com.im.pemkos.network.response.LoginData
import com.im.pemkos.ui.TenantMainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {
    private lateinit var presenter: LoginPresenter
    private lateinit var sharedPref: SharedPreferences
// login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        presenter =  LoginPresenter(this, this)


        btn_login.setOnClickListener {
            val email = edt_email.text.toString()
            val pswrd = edt_password.text.toString()
            if(email.isNullOrEmpty() || pswrd.isNullOrEmpty()){
                Toast.makeText(this, "Nama atau NoHp dan password harus diisi", Toast.LENGTH_LONG).show()
            } else presenter.login(email, pswrd)
        }

    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setData(data: LoginData) {

        sharedPref.edit().putBoolean("IS_LOGIN", true).apply()
        sharedPref.edit().putString("ID", data.id).apply()
        sharedPref.edit().putString("ROLE", data.role).apply()

        Log.d("LoginActivity", "LOGIN BERHASIL $data")

        val intent = if(data.role == "admin") Intent(this, MainActivity::class.java) else Intent(this, TenantMainActivity::class.java)
        startActivity(intent)

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }
}