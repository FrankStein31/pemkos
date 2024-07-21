package com.im.pemkos.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.im.pemkos.R
import com.im.pemkos.ui.kost.KostActivity
import com.im.pemkos.ui.login.LoginActivity
import com.im.pemkos.ui.main_fragment.BillFragment
import com.im.pemkos.ui.main_fragment.PaymentFragment
import com.im.pemkos.ui.main_fragment.RoomFragment
import com.im.pemkos.ui.main_fragment.TenantFragment
import com.im.pemkos.ui.profil.ubahpassword.UbahPasswordActivity
import com.im.pemkos.ui.room.add.AddRoomActivity
import com.im.pemkos.ui.tenant.add.AddTenantActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private var content: FrameLayout? = null
    private var addMenu : MenuItem? = null
    private  var billMenu : MenuItem? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_room -> {
                val fragment = RoomFragment()
                addFragment(fragment)
                toolbar_title.text = "Kamar"
                addMenu?.setVisible(true)
                billMenu?.setVisible(false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_tenant -> {
                val fragment = TenantFragment()
                addFragment(fragment)
                toolbar_title.text = "Anak Kos"
                addMenu?.setVisible(true)
                billMenu?.setVisible(false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_bill -> {
                val fragment = BillFragment()
                addFragment(fragment)
                toolbar_title.text = "Tagihan"
                addMenu?.setVisible(false)
                billMenu?.setVisible(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_payment -> {
                val fragment = PaymentFragment()
                addFragment(fragment)
                toolbar_title.text = "Pembayaran"
                addMenu?.setVisible(false)
                billMenu?.setVisible(false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_title.text = "Kamar"
//        toolbar.overflowIcon = AppCompatResources.getDrawable(this, R.drawable.ic_option_menu)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = RoomFragment()
        addFragment(fragment)

        checkSignedInOrNot()





    }



    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commit()
    }




    public override fun onStart() {
        super.onStart()


    }

    private fun checkSignedInOrNot(){
        val islogin = sharedPref.getBoolean("IS_LOGIN", false)
        val role = sharedPref.getString("ROLE", "")
        if(!islogin){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else if(role == "anak kos"){
            val intent = Intent(this, TenantMainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        addMenu = menu?.getItem(0)
        billMenu = menu?.getItem(1)
        billMenu?.setVisible(false)
        billMenu?.setOnMenuItemClickListener {
            var fragment = supportFragmentManager.findFragmentById(R.id.content) as BillFragment
            fragment.onBillMenuClick()
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_add -> {
                if(navigation.selectedItemId == R.id.action_room){
                    val intent = Intent(this, AddRoomActivity::class.java)
                    startActivity(intent)
                }else if(navigation.selectedItemId == R.id.action_tenant){
                    val intent = Intent(this, AddTenantActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.menu_kost -> {
                val intent = Intent(this, KostActivity::class.java)
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




}