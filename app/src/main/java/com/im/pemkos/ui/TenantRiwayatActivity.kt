package com.im.pemkos.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.im.pemkos.R
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.network.response.Bill
import com.im.pemkos.network.response.BillUserResponse
import com.im.pemkos.network.response.BillUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activiry_riwayat_tagihan.*
import retrofit2.HttpException

class TenantRiwayatActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var tenantId: String
    private lateinit var adapter: RiwayatTagihanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiry_riwayat_tagihan)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Menampilkan tombol kembali
            setDisplayShowTitleEnabled(false) // Menghilangkan judul default
        }
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        tenantId = sharedPref.getString("ID", "") ?: ""

        adapter = RiwayatTagihanAdapter(emptyList(), object : RiwayatTagihanAdapter.OnItemClickListener {
            override fun onItemClick(billUser: Bill?) {
//                Toast.makeText(this@TenantRiwayatActivity, "Tanggal bayar : ${billUser?.billDate}", Toast.LENGTH_SHORT).show()
            }
        })

        riwayat_tagihan.layoutManager = LinearLayoutManager(this)
        riwayat_tagihan.adapter = adapter

        loadBillHistory()
    }

    private fun loadBillHistory() {
        ApiService.client.create(ApiInteractor::class.java).billUser(tenantId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.status) {
                    val billUsers = response.data
                    adapter.updateData(billUsers)
                } else {
                    Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }, { throwable ->
                if (throwable is HttpException) {
                    Toast.makeText(this, "Error: ${throwable.message()}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
