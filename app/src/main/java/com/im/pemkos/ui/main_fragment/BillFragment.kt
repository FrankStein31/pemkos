package com.im.pemkos.ui.main_fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import com.im.pemkos.ui.bill.BillListAdapter
import com.im.pemkos.ui.bill.BillPresenter
import com.im.pemkos.ui.bill.BillView
import com.im.pemkos.ui.tenant.detail.DetailTenantActivity
import com.im.pemkos.utils.NewBillDialog
import kotlinx.android.synthetic.main.fragment_bill.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BillFragment : Fragment(), BillView {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var presenter: BillPresenter

    private lateinit var adapterList: BillListAdapter
    private var listBill = mutableListOf<Bill>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filterButton: Button = view.findViewById(R.id.btn_filter)

        filterButton.setOnClickListener {
            showFilterBottomSheet()
        }
        activity?.let {


            sharedPref = PreferenceManager.getDefaultSharedPreferences(it)
            presenter = BillPresenter(it, this)
            presenter.getBill()

            swipe_layout.setOnRefreshListener {
                presenter.getBill()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipe_layout.setColorSchemeColors(it.getColor(R.color.red_dark), it.getColor(R.color.red))
            }

            adapterList = BillListAdapter(
                listBill,
                clickListener = { item ->
                }
            )

            initViewRecyclerView()
        }
    }
    //menmapilkan filter
    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_sheet_filter, null)

        val radioGroupStatus: RadioGroup = view.findViewById(R.id.radio_group_status)
        val spinnerMonth: Spinner = view.findViewById(R.id.spinner_month)
        val spinnerYear: Spinner = view.findViewById(R.id.spinner_year)
        val applyButton: Button = view.findViewById(R.id.btn_apply_filter)

        // Get current month and year
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // January is 0
        val currentYear = calendar.get(Calendar.YEAR)

        // Set default values for the spinners
        spinnerMonth.setSelection(currentMonth) // Set to the current month

        // Find the position of the current year in the years array
        val yearsArray = resources.getStringArray(R.array.years_array)
        val currentYearPosition = yearsArray.indexOf(currentYear.toString())
        if (currentYearPosition >= 0) {
            spinnerYear.setSelection(currentYearPosition) // Set to the current year
        }

        applyButton.setOnClickListener {
            val selectedStatusId = radioGroupStatus.checkedRadioButtonId
            val selectedMonth = spinnerMonth.selectedItem.toString()
            val selectedYear = spinnerYear.selectedItem.toString()

            val status = when (selectedStatusId) {
                R.id.radio_lunas -> "lunas"
                R.id.radio_belum_bayar -> "belum bayar"
                else -> "all"
            }

            presenter.getFilterBill(status, selectedMonth, selectedYear)


            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun showBill(listBill: List<Bill>) {
        swipe_layout.isRefreshing = false

        this.listBill.clear()
        this.listBill.addAll(listBill)
        adapterList.notifyDataSetChanged()
        view_no_data.visibility = if (listBill.isEmpty()) View.VISIBLE else View.GONE

    }

    private fun initViewRecyclerView(){

        rv_bill.layoutManager = LinearLayoutManager(activity)
        rv_bill.adapter = adapterList

    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroyed()
    }

    override fun onResume() {
        super.onResume()
        presenter.getBill()
    }

    fun onBillMenuClick(){

        activity?.let {

            var newBillDialog: NewBillDialog = NewBillDialog(it) { m, y ->
                presenter.generateBill(m, y)
            }
            newBillDialog.show()
        }
    }
}