package com.im.pemkos.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import com.im.pemkos.network.response.CheckoutResponse
import com.im.pemkos.ui.bill.BillByIdView
import com.im.pemkos.ui.bill.BillListAdapter
import com.im.pemkos.ui.bill.BillListTenantAdapter
import com.im.pemkos.ui.bill.BillPresenter
import com.im.pemkos.ui.bill.BillView
import com.im.pemkos.ui.login.LoginActivity
import com.im.pemkos.ui.main_fragment.BillFragment
import com.im.pemkos.ui.midtrans.MidtransActivity
import com.im.pemkos.ui.profil.ubahpassword.UbahPasswordActivity
import com.im.pemkos.ui.tenant.BillByIdPresenter
import com.im.pemkos.ui.tenant.PaymentPresenter
import com.im.pemkos.ui.tenant.PaymentView
import com.im.pemkos.ui.tenant.detail.DetailTenantActivity
import kotlinx.android.synthetic.main.activity_main_tenant.*
import kotlinx.android.synthetic.main.fragment_bill.rv_bill
import kotlinx.android.synthetic.main.fragment_bill.swipe_layout
import kotlinx.android.synthetic.main.fragment_bill.view_no_data


class TenantMainActivity : AppCompatActivity(), BillView, PaymentView {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var id: String
    private lateinit var paymentPresenter: PaymentPresenter
    private lateinit var presenter: BillPresenter


    private lateinit var adapterList: BillListTenantAdapter
    private var listBill = mutableListOf<Bill>()


    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tenant)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        checkSignedInOrNot()
        id = sharedPref.getString("ID", "") ?: ""
        presenter = BillPresenter(this, this)
        presenter.getBillUser(id)
        Log.e("userbill", "ID = " + id)

        paymentPresenter = PaymentPresenter(this, this)

        swipe_layout.setOnRefreshListener {
            presenter.getBillUser(id)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            swipe_layout.setColorSchemeColors(this.getColor(R.color.red_dark), this.getColor(R.color.red))
        }

        adapterList = BillListTenantAdapter(
            listBill,
            clickListener = { item ->
            if(item.status == "0"){
                showPaymentOptionsDialog(item.idBill)
            } else Toast.makeText(this, "Pembayaran sudah dilakukan", Toast.LENGTH_SHORT).show()
            }
        )

        initViewRecyclerView()
        rv_bill.post {
            rv_bill.scrollToPosition(0)
        }
    }
    public override fun onStart() {
        super.onStart()
    }

    private fun checkSignedInOrNot(){
        val islogin = sharedPref.getBoolean("IS_LOGIN", false)
        if(!islogin){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun showPaymentOptionsDialog(idBill: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Silahkan pilih metode pembayaran!")

        // Add the buttons
        builder.setPositiveButton("Cash") { dialog, _ ->
            // Direct to the "Transaction Successful" page
            navigateToTransactionSuccess(idBill)
            dialog.dismiss()
        }
        builder.setNegativeButton("Online") { dialog, _ ->
            // Process the payment online
            paymentPresenter.processPayment(idBill)

            dialog.dismiss()
        }

        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToTransactionSuccess(idBill: String) {
        // ketika pembayaran sukses maka menuju ke halaman tampilan sukses pembayaran
        val intent = Intent(this, TransaksiBerhasilActivity::class.java)
        intent.putExtra("IDBill", idBill)
        intent.putExtra("METODEPEMBAYARAN", "cash")
        startActivity(intent)
        Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_tenant, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.riwayat_tagihan -> {
                val intent = Intent(this, TenantRiwayatActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.menu_logout -> {
                sharedPref.edit().clear().apply()
                checkSignedInOrNot()
                true
            }
            R.id.menu_password -> {
                val intent = Intent(this, UbahPasswordActivity::class.java)
                startActivity(intent)
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun showPaymentSuccess(response: CheckoutResponse, idBill: String) {
        // menuju ke tampilan midtrans untuk melakukan transaksi
        if(response.status!!){
            if(response.snapToken!!.isNotEmpty()){
                val url = "https://app.sandbox.midtrans.com/snap/v4/redirection" + "/${response.snapToken}"
                val intent = Intent(this, MidtransActivity::class.java)
                intent.putExtra("IDBill", idBill)
                intent.putExtra("URL", url)
                startActivity(intent)
            }
        }
    }


    override fun showMessagepayment(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
//
//
//    override fun showMessageBill(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }

//    override fun showBillById(data: DataBill) {
        // untuk menampilkan data anak kos
//        val item = findViewById<LinearLayout>(R.id.item)
//        val title = findViewById<TextView>(R.id.tv_title)
//        val date = findViewById<TextView>(R.id.tv_date)
//        val desc = findViewById<TextView>(R.id.tv_desc)
//        val nominal = findViewById<TextView>(R.id.tv_nominal)
//        val status = findViewById<TextView>(R.id.tv_status)
//
//        view_no_data.visibility = if (data.nama == null) View.VISIBLE else View.GONE
//
//
//        title.text = data.nama
//        date.text = data.tglBill
//        desc.text = "${data.kode} ${data.tipe}"
//        nominal.text = "Rp. ${data.nominal}"
//        status.text = if(data.status == "0")  "Belum bayar" else "Lunas"
//        if(data.status == "0"){
//            status.setTextColor(Color.parseColor("#A13842"));
//        }
//
//
//        // klik item untuk pembayaran
//        item.setOnClickListener {
//            if(data.status == "0"){
//                showPaymentOptionsDialog(data.idAnakKos!!)
//            } else Toast.makeText(this, "Pembayaran sudah dilakukan", Toast.LENGTH_SHORT).show()
//        }

//    }

    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

//    override fun showBill(listBill: List<Bill>) {
//        swipe_layout.isRefreshing = false
//
//        this.listBill.clear()
//        this.listBill.addAll(listBill)
//        adapterList.notifyDataSetChanged()
//        view_no_data.visibility = if (listBill.isEmpty()) View.VISIBLE else View.GONE
//    }

    override fun showBill(listBill: List<Bill>) {
        swipe_layout.isRefreshing = false

        val filteredBills = listBill.filter { it.metodePembayaran == null }

        this.listBill.clear()
        this.listBill.addAll(filteredBills)
        adapterList.notifyDataSetChanged()

        if (filteredBills.isEmpty()) {
            view_no_data.visibility = View.VISIBLE
            rv_bill.visibility = View.GONE
        } else {
            view_no_data.visibility = View.GONE
            rv_bill.visibility = View.VISIBLE
        }
    }
    private fun initViewRecyclerView(){
        rv_bill.layoutManager = LinearLayoutManager(this)
        rv_bill.adapter = adapterList

    }


}