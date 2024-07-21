package com.im.pemkos.ui.room.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Kost
import com.im.pemkos.model.Room
import com.im.pemkos.network.request.AddRoomRequest
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.activity_add_room.btn_submit
import kotlinx.android.synthetic.main.activity_add_room.btn_take_image
import kotlinx.android.synthetic.main.activity_add_room.edt_code
import kotlinx.android.synthetic.main.activity_add_room.iv_photo
import kotlinx.android.synthetic.main.activity_add_room.rb_female
import kotlinx.android.synthetic.main.activity_add_room.rb_male
import kotlinx.android.synthetic.main.activity_add_room.toolbar
import kotlinx.android.synthetic.main.activity_add_tenant.*
import java.io.ByteArrayOutputStream
import java.util.*


class EditRoomActivity : AppCompatActivity(), EditRoomView {

    private lateinit var presenter: EditRoomPresenter
    private lateinit var sharedPref: SharedPreferences
    private var idSekolah = ""
    private var room: Room? = null
    private var photo: String? = null
    private var listKost: List<Kost> = mutableListOf()



    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        idSekolah = sharedPref.getString("ID_SEKOLAH", "") ?: ""

        room = intent.getParcelableExtra(EXTRA_DATA)
        presenter = EditRoomPresenter(this, this)
        presenter.getKost()
        setData()
    }

    private fun setData(){

        room?.let{
            edt_code.setText(it.code)
            edt_type.setText(it.type)
            edt_facilites.setText(it.facilities)
            edt_price.setText(it.price)
            if (it.gender=="L") rb_male.isChecked = true else rb_female.isChecked = true

            if(!it.photo.isNullOrEmpty()){
                iv_photo.visibility = View.VISIBLE
                Glide
                        .with(this)
                        .load(it.photo)
                        .into(iv_photo)
            }
        }

        btn_take_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
        }




        btn_submit.setOnClickListener {
            val code = edt_code.text.toString()
            val type = edt_type.text.toString()
            val facilities = edt_facilites.text.toString()
            val price = edt_price.text.toString()
            val gender: String? = if(rb_male.isChecked) "L" else if(rb_female.isChecked) "P" else null

            if (code.isNullOrEmpty() || type.isNullOrEmpty() || price.isNullOrEmpty() || facilities.isNullOrEmpty() || gender.isNullOrEmpty()){
                showMessage("Semua kolom harus diisi !")
            }else{
                var kostId = listKost[sp_kos.selectedItemPosition].idKost
                val data = AddRoomRequest(
                    idRoom = room?.idRoom,
                    code = code,
                    idKost = kostId,
                    type = type,
                    gender = gender,
                    facilities = facilities,
                    price = price,
                    photo = photo
                )
                presenter.editRoom(data)
            }
        }
        toolar_title.text = "Ubah Kamar"
        btn_submit.text = "UBAH"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var imageUri: Uri? = result.data?.data
            imageUri?.let {
                iv_photo.setImageURI(it)
                iv_photo.visibility = View.VISIBLE
                photo = encoder(imageUri)
            }



        }
    }

    private fun encoder(imageUri: Uri): String {
        val input = this.contentResolver.openInputStream(imageUri)
        //val bm = BitmapFactory.decodeResource(resources, R.drawable.test)
        val image = BitmapFactory.decodeStream(input, null, null)
        //encode image to base64 string
        val baos = ByteArrayOutputStream()
        //bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()

        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP)
        //return Base64.getEncoder().encodeToString(imageBytes) // Not Worked, too.
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun successEdit() {
        showMessage("Kamar berhasil diubah !")
        finish()
    }

    override fun showKost(kost: List<Kost>) {
        this.listKost = kost
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, kost.map {
                it.name
            })
        sp_kos.adapter = adapter

        if(room?.idKost!=null){
            sp_room.setSelection(listKost.indexOfFirst { e -> e.idKost == room!!.idKost })
        }
    }

}