package com.im.pemkos.ui.tenant.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Room
import com.im.pemkos.model.Tenant
import com.im.pemkos.network.request.AddTenantRequest
import kotlinx.android.synthetic.main.activity_add_tenant.*
import java.io.ByteArrayOutputStream
import java.util.*


class EditTenantActivity : AppCompatActivity(), EditTenantView {

    private lateinit var presenter: EditTenantPresenter
    private lateinit var sharedPref: SharedPreferences
    private var tenant: Tenant? = null
    private var photo: String? = null
    private var rooms: List<Room> = mutableListOf()



    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tenant)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        tenant = intent.getParcelableExtra(EXTRA_DATA)
        presenter = EditTenantPresenter(this, this)
        presenter.getRoom()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        edt_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yearPicked, monthOfYear, dayOfMonth ->

                edt_date.setText(
                    String.format("%s-%s-%s", yearPicked, monthOfYear, dayOfMonth)
                )
            }, year, month, day)

            datePickerDialog.show()
        }
        setData()
    }

    private fun setData(){

        tenant?.let{
            edt_name.setText(it.name)
            edt_code.setText(it.code)
            edt_address.setText(it.address)
            edt_telephone.setText(it.contact)
            edt_date.text = it.entryDate
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
            val name = edt_name.text.toString()
            val code = edt_code.text.toString()
            val telephone = edt_telephone.text.toString()
            val address = edt_address.text.toString()
            val entryDate = edt_date.text.toString()
            val gender: String? = if(rb_male.isChecked) "L" else if(rb_female.isChecked) "P" else null

            if (name.isNullOrEmpty() || code.isNullOrEmpty()  || address.isNullOrEmpty() || telephone.isNullOrEmpty() || gender.isNullOrEmpty()){
                showMessage("Semua kolom harus diisi !")
            }else{
                var roomId = rooms[sp_room.selectedItemPosition].idRoom
                Log.e("klikitem", "klik room = " + roomId)
                val data = AddTenantRequest(
                    idTenant = tenant?.idTenant,
                    idRoom = roomId,
                    name = name,
                    code = code,
                    address = address,
                    gender = gender,
                    contact = telephone,
                    entryDate = entryDate,
                    photo = photo
                )
                presenter.editTenant(data)
            }
        }
        toolbar_title.text = "Ubah Data Anak Kos"
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
        showMessage("Data Anak Kos berhasil diubah !")
        finish()
    }


    override fun showRoom(rooms: List<Room>) {
        this.rooms = rooms

        // Filter rooms with status_kamar == "0" and include tenant's room even if status_kamar == "1"
        val availableRooms = rooms.filter {
            it.idRoom == tenant?.idRoom
        }

        val displayRooms = availableRooms.map { "${it.code} - ${it.type} @Rp. ${it.price}" }


        // Set the filtered or fallback rooms to the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, displayRooms)
        sp_room.adapter = adapter

        // Set selection if tenant's room id matches and availableRooms is not empty
        if (tenant?.idRoom != null) {
            sp_room.setSelection(availableRooms.indexOfFirst { e -> e.idRoom == tenant!!.idRoom })
        }
        this.rooms = availableRooms
    }


}