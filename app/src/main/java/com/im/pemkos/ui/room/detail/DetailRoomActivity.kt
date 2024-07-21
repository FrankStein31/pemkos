package com.im.pemkos.ui.room.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Room
import com.im.pemkos.ui.room.edit.EditRoomActivity
import kotlinx.android.synthetic.main.activity_detail_room.*


class DetailRoomActivity : AppCompatActivity(), DetailRoomView {

    private var room: Room? = null
    private lateinit var presenter: DetailTeacherPresenter



    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_room)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        room = intent.getParcelableExtra(EXTRA_DATA)
        presenter = DetailTeacherPresenter(this, this)

        setData()
    }

    private fun setData(){
        room?.let{
            toolbar.title = it.code
            tv_type.text = it.type
            tv_facilites.text = it.facilities
            tv_price.text = "Rp. ${it.price}"
            tv_gender.text = if (it.gender=="L") "Laki - laki" else "Perempuan"
            if(!it.photo.isNullOrEmpty()){
                Glide
                .with(this)
                .load(it.photo)
                .into(iv_photo)
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
                val intent = Intent(this, EditRoomActivity::class.java)
                intent.putExtra(EXTRA_DATA, room)
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
        showMessage("Berhasil hapus data kamar")
        finish()
    }

    private fun deleteData(){
        var alert: AlertDialog? = null
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Anda yakin ingin menghapus data ${room?.code} ?")
        builder.setNegativeButton("Ya") { _, _ ->
            room?.idRoom?.let { presenter.deleteRoom(it) }
        }

        builder.setPositiveButton("Tidak") { _, _ ->
            alert?.dismiss()
        }


        alert = builder.show()
    }

}