package com.im.pemkos.ui.profil

import android.app.Activity
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.network.response.Profil
import kotlinx.android.synthetic.main.activity_profil.*
import java.io.ByteArrayOutputStream


class ProfilActivity : AppCompatActivity(), ProfilView {
    private lateinit var sharedPref: SharedPreferences
    private var newPhoto: String? = null
    private lateinit var id: String
    private lateinit var presenter: ProfilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        presenter = ProfilPresenter(this, this)
        setData()

        btn_take_image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
        }

        btn_submit.setOnClickListener {
            val name = edt_name.text.toString()
            val headName = edt_kepsek.text.toString()
            val address = edt_address.text.toString()
            val email = edt_email.text.toString()
            if(name.isNullOrEmpty() || headName.isNullOrEmpty() || address.isNullOrEmpty() || email.isNullOrEmpty() ){
                showMessage("Inputan belum lengkap")
            } else presenter.ubahProfil(name = name, headName = headName, photo = newPhoto, id = id, address = address, email = email)
        }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }


    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun successUbahProfil(data: Profil?) {
        data?.let {
            sharedPref.edit().putString("NAMA_SEKOLAH", data.namaSekolah).apply()
            sharedPref.edit().putString("NAMA_KEPALA_SEKOLAH", data.namaKepalaSekolah).apply()
            sharedPref.edit().putString("ALAMAT_SEKOLAH", data.alamat).apply()
            sharedPref.edit().putString("FOTO_SEKOLAH", data.foto).apply()
            sharedPref.edit().putString("EMAIL_SEKOLAH", data.email).apply()
        }
        showMessage("Profil berhasil diubah")
        finish()
    }

    private fun setData(){

        id = sharedPref.getString("ID_SEKOLAH","") ?: ""
        val schoolName = sharedPref.getString("NAMA_SEKOLAH","")
        val headSchoolName = sharedPref.getString("NAMA_KEPALA_SEKOLAH","")
        val address = sharedPref.getString("ALAMAT_SEKOLAH","")
        val photo = sharedPref.getString("FOTO_SEKOLAH","")
        val email = sharedPref.getString("EMAIL_SEKOLAH","")

        edt_name.setText(schoolName)
        edt_kepsek.setText(headSchoolName)
        edt_email.setText(email)
        edt_address.setText(address)
        if(!photo.isNullOrEmpty()){
            Log.d("ProfilActivity", "wHAT WRONG $photo")
            iv_photo.visibility = View.VISIBLE
            Glide
                    .with(this)
                    .load(photo)
                    .into(iv_photo)
        }

    }



    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var imageUri: Uri? = result.data?.data
            imageUri?.let {
                Log.d("ProfilActivity", "uri $it")
                iv_photo.visibility = View.VISIBLE
                newPhoto = encoder(imageUri)
                iv_photo.setImageURI(it)
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



}
