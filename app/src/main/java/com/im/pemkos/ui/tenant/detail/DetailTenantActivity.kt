package com.im.pemkos.ui.tenant.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Tenant
import com.im.pemkos.ui.tenant.edit.EditTenantActivity
import kotlinx.android.synthetic.main.activity_detail_tenant.*


class DetailTenantActivity : AppCompatActivity(), DetailTenantView {

    private var tenant: Tenant? = null
    private lateinit var presenter: DetailTenantPresenter



    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tenant)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tenant = intent.getParcelableExtra(EXTRA_DATA)
        presenter = DetailTenantPresenter(this, this)

        setData()
    }

    private fun setData(){
        tenant?.let{
            tv_name.text = it.name
            tv_code.text = it.code
            tv_address.text = it.address
            tv_telephone.text = it.contact
            tv_gender.text = if (it.gender=="L") "Laki - laki" else "Perempuan"
            tv_date.text = it.entryDate

            if(it.gender == "L") iv_placeholder.setImageResource(R.drawable.ic_man_kid)
            if(!it.photo.isNullOrEmpty()){
                Glide
                .with(this)
                .load(it.photo)
                .into(iv_photo)
            }

            // room
            if(it.idRoom!=null){
                tv_room.visibility = View.GONE
                tv_room_code.text = "${it.roomCode} - ${it.type}"
                tv_desc_room.text = it.facilities
                tv_room_price.text = "Rp. ${it.price}"
                if(!it.roomPhoto.isNullOrEmpty()){
                    Glide
                        .with(this)
                        .load(it.roomPhoto)
                        .into(iv_room_photo)
                }

            }else{
                item_room.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_edit -> {
                Log.d("DetailActivity", "masuk menu edit")
                val intent = Intent(this, EditTenantActivity::class.java)
                intent.putExtra(EXTRA_DATA, tenant)
                startActivity(intent)
                true
            }
            R.id.menu_delete -> {
                deleteData()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun successDelete() {
        showMessage("Berhasil hapus data Anak Kos")
        finish()
    }

    private fun deleteData(){
        var alert: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Anda yakin ingin menghapus data ${tenant?.name} ?")
        builder.setNegativeButton("Ya") { _, _ ->
            tenant?.idTenant?.let { presenter.deleteTenant(it) }
        }

        builder.setPositiveButton("Tidak") { _, _ ->
            alert?.dismiss()
        }


        alert = builder.show()
    }

}