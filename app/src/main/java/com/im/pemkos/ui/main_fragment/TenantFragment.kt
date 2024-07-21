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
import com.im.pemkos.model.Tenant
import com.im.pemkos.ui.tenant.TenantListAdapter
import com.im.pemkos.ui.tenant.TenantPresenter
import com.im.pemkos.ui.tenant.TenantView
import com.im.pemkos.ui.tenant.detail.DetailTenantActivity
import kotlinx.android.synthetic.main.fragment_tenant.*

class TenantFragment : Fragment(), TenantView {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var presenter: TenantPresenter

    private lateinit var adapterList: TenantListAdapter
    private var listAbk = mutableListOf<Tenant>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("userbill", "MASUK EK TENANT FRAGMENT")

        activity?.let {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(it)
            presenter = TenantPresenter(it, this)
            presenter.getTenant()

            swipe_layout.setOnRefreshListener {
                presenter.getTenant()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipe_layout.setColorSchemeColors(it.getColor(R.color.red_dark), it.getColor(R.color.red))
            }
            adapterList = TenantListAdapter(
                listAbk,
                clickListener = { item ->
                    val intent = Intent(it, DetailTenantActivity::class.java)
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
        return inflater.inflate(R.layout.fragment_tenant, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TenantFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showAbk(listAbk: List<Tenant>) {
        swipe_layout.isRefreshing = false

        this.listAbk.clear()
        this.listAbk.addAll(listAbk)
        adapterList.notifyDataSetChanged()
        view_no_data.visibility = if (listAbk.isEmpty()) View.VISIBLE else View.GONE

    }

    private fun initViewRecyclerView(){

        rv_tenant.layoutManager = LinearLayoutManager(activity)
        rv_tenant.adapter = adapterList

    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

    override fun onResume() {
        super.onResume()
        presenter.getTenant()
    }

}