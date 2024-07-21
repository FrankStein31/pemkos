package com.im.pemkos.ui.main_fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.im.pemkos.R
import com.im.pemkos.model.Room
import com.im.pemkos.ui.room.RoomListAdapter
import com.im.pemkos.ui.room.RoomPresenter
import com.im.pemkos.ui.room.RoomView
import com.im.pemkos.ui.room.detail.DetailRoomActivity
import kotlinx.android.synthetic.main.fragment_room.*

class RoomFragment : Fragment(), RoomView {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var presenter: RoomPresenter

    lateinit var adapterList: RoomListAdapter
    private var listTeachers = mutableListOf<Room>()
    private var idSekolah = ""

    companion object{
        const val TAG = "MainActivity"
        const val EXTRA_DATA = "EXTRA_DATA"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(it)
            idSekolah = sharedPref.getString("ID_SEKOLAH", "") ?: ""

            presenter = RoomPresenter(it, this)
            presenter.getRoom()



            swipe_layout.setOnRefreshListener {
                presenter.getRoom()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipe_layout.setColorSchemeColors(it.getColor(R.color.red_dark), it.getColor(R.color.red))
            }





            adapterList = RoomListAdapter(
                listTeachers,
                clickListener = { item ->
                    val intent = Intent(it, DetailRoomActivity::class.java)
                    intent.putExtra("EXTRA_DATA", item)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)
                }
            )

            initViewRecyclerView()


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
    }




    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showRoom(teachers: List<Room>) {
        swipe_layout.isRefreshing = false
        this.listTeachers.clear()
        this.listTeachers.addAll(teachers)
        adapterList.notifyDataSetChanged()
        view_no_data.visibility = if (teachers.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

    private fun initViewRecyclerView(){

        rv_room.layoutManager = LinearLayoutManager(activity)
        rv_room.adapter = adapterList

    }

    override fun onResume() {
        super.onResume()
        presenter.getRoom()
    }
}