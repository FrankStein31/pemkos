package com.im.pemkos.ui.kost

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.im.pemkos.R
import com.im.pemkos.model.Kost
import com.im.pemkos.ui.login.LoginActivity
import com.im.pemkos.ui.profil.ubahpassword.UbahPasswordActivity
import com.im.pemkos.utils.NewBillDialog
import com.im.pemkos.utils.NewKosDialog
import kotlinx.android.synthetic.main.activity_kost.*


class KostActivity : AppCompatActivity(), KostView {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var id: String
    private lateinit var presenter: KostPresenter

    private lateinit var adapterList: KostListAdapter
    private var listKost = mutableListOf<Kost>()


    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kost)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        checkSignedInOrNot()
        presenter = KostPresenter(this, this)
        presenter.getKost()

        swipe_layout.setOnRefreshListener {
            presenter.getKost()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            swipe_layout.setColorSchemeColors(this.getColor(R.color.red_dark), this.getColor(R.color.red))
        }
        adapterList =KostListAdapter(
            listKost,
            clickListener = { item ->
                var alert: AlertDialog? = null
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Konfirmasi")
                builder.setMessage("Anda yakin ingin menghapus data ${item.name} ?")
                builder.setNegativeButton("Ya") { _, _ ->
                    item.idKost?.let { presenter.deleteKost(it) }
                }

                builder.setPositiveButton("Tidak") { _, _ ->
                    alert?.dismiss()
                }


                alert = builder.show()
            }
        )

        initViewRecyclerView()

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





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_data, menu)
        
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_add -> {
                var newBillDialog: NewKosDialog = NewKosDialog(this) { it ->
                    presenter.addKos(it)
                }
                newBillDialog.show()
                true
            }


            else -> return super.onOptionsItemSelected(item)
        }
    }



    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun successAdd() {
        showMessage("Data Kos berhasil ditambah !")
        presenter.getKost()
    }

    override fun successDelete() {
        showMessage("Data Kos berhasil dihapus !")
        presenter.getKost()
    }

    override fun showKost(list: List<Kost>) {
        swipe_layout.isRefreshing = false

        this.listKost.clear()
        this.listKost.addAll(list)
        adapterList.notifyDataSetChanged()
        view_no_data.visibility = if (listKost.isEmpty()) View.VISIBLE else View.GONE

    }

    private fun initViewRecyclerView(){

        rv_kost.layoutManager = LinearLayoutManager(this)
        rv_kost.adapter = adapterList

    }

}