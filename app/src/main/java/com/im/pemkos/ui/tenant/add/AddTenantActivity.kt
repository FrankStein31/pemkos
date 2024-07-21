package com.im.pemkos.ui.tenant.add

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
import com.im.pemkos.R
import com.im.pemkos.model.Room
import com.im.pemkos.model.Tenant
import com.im.pemkos.network.request.AddTenantRequest
import kotlinx.android.synthetic.main.activity_add_tenant.*
import java.io.ByteArrayOutputStream
import java.util.*


class AddTenantActivity : AppCompatActivity(), AddTenantView {

    private lateinit var presenter: AddTenantPresenter
    private lateinit var sharedPref: SharedPreferences
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

        presenter = AddTenantPresenter(this, this)
        presenter.getRoom()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        edt_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, yearPicked, monthOfYear, dayOfMonth ->

                edt_date.text = String.format("%s-%s-%s", yearPicked, monthOfYear+1, dayOfMonth)
            }, year, month, day)

            datePickerDialog.show()
        }

        setData()
    }

    private fun setData(){
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

            if (name.isNullOrEmpty() || code.isNullOrEmpty()  || address.isNullOrEmpty() || entryDate.isNullOrEmpty() || telephone.isNullOrEmpty() || gender.isNullOrEmpty() ){
                showMessage("Semua kolom harus diisi !")
            }else{
                if(rooms.isNotEmpty()){
                    var roomId = rooms[sp_room.selectedItemPosition].idRoom
                    Log.e("klikitem", "klik room = " + roomId)
                    val data = AddTenantRequest(
                        idRoom = roomId,
                        name = name,
                        code = code,
                        address = address,
                        gender = gender,
                        contact = telephone,
                        entryDate = entryDate,
                        photo = photo
                    )
                    presenter.addTenant(data)

                }else showMessage("Maaf Kamar masih penuh semua !")

            }
        }
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

    override fun successAdd() {
        showMessage("Data Anak Kos berhasil ditambah !")
        finish()
    }

    override fun showRoom(rooms: List<Room>) {
        // Filter rooms with status_kamar == 0
        val availableRooms = rooms.filter { it.statusKamar == "0" }
        this.rooms = availableRooms

        // Check if the list is empty and add fallback item if necessary
        val displayRooms = if (availableRooms.isEmpty()) {
            listOf("Kamar penuh semua")
        } else {
            availableRooms.map { "${it.code} - ${it.type} @Rp. ${it.price}" }
        }

        // Set the filtered or fallback rooms to the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, displayRooms)
        sp_room.adapter = adapter
    }


}