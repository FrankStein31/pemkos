package com.im.pemkos.ui.main_fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import com.im.pemkos.ui.bill.BillListAdapter
import com.im.pemkos.ui.bill.BillPresenter
import com.im.pemkos.ui.bill.BillView
import com.im.pemkos.ui.bill.HistoryPaymentAdapter
import com.im.pemkos.ui.tenant.detail.DetailTenantActivity
import com.im.pemkos.utils.NewBillDialog
import kotlinx.android.synthetic.main.fragment_bill.rv_bill
import kotlinx.android.synthetic.main.fragment_bill.swipe_layout
import kotlinx.android.synthetic.main.fragment_bill.view_no_data
import kotlinx.android.synthetic.main.fragment_payment.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PaymentFragment : Fragment(), BillView {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var presenter: BillPresenter

    private lateinit var historyPaymentAdapter: HistoryPaymentAdapter
    private var listBill = mutableListOf<Bill>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filterButton: Button = view.findViewById(R.id.btn_filter_pembayaran)
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

            historyPaymentAdapter = HistoryPaymentAdapter(
                listBill,
                clickListener = { item ->
//                    val intent = Intent(it, DetailTenantActivity::class.java)
//                    intent.putExtra("EXTRA_DATA", item)
//                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//                    startActivity(intent)
                }
            )

            initViewRecyclerView()
        }
    }

    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view: View = LayoutInflater.from(requireActivity()).inflate(R.layout.filter_pembayaran, null)

        val radioGroupMetode: RadioGroup = view.findViewById(R.id.radio_group_metode)
        radioGroupMetode.visibility = View.GONE
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
            val selectedStatusId = radioGroupMetode.checkedRadioButtonId
            val selectedMonth = spinnerMonth.selectedItem.toString()
            val selectedYear = spinnerYear.selectedItem.toString()

            val metode = when (selectedStatusId) {
                R.id.radio_cash -> "cash"
                R.id.radio_online -> "online"
                else -> "all"
            }

            presenter.getFilterBill(metode, selectedMonth, selectedYear)


            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    override fun showMessage(message: String) {
        swipe_layout.isRefreshing = false
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

//    override fun showBill(listBill: List<Bill>) {
//        swipe_layout.isRefreshing = false
//
//        this.listBill.clear()
//
//        // Filter the list to add only items where metode_pembayaran is not null
//        val filteredListBill = listBill.filter { it.metodePembayaran != null }
//        this.listBill.addAll(filteredListBill)
//
//        historyPaymentAdapter.notifyDataSetChanged()
//        view_no_data.visibility = if (filteredListBill.isEmpty()) View.VISIBLE else View.GONE
//    }
//    override fun showBill(listBill: List<Bill>) {
//        swipe_layout.isRefreshing = false
//
//        this.listBill.clear()
//
//        // Filter the list to add only items where metode_pembayaran is not null
//        val filteredListBill = listBill.filter { it.metodePembayaran != null }
//        this.listBill.addAll(filteredListBill)
//
//        historyPaymentAdapter.notifyDataSetChanged()
//        view_no_data.visibility = if (filteredListBill.isEmpty()) View.VISIBLE else View.GONE
//
//        // Calculate and display total income
//        val totalIncome = calculateTotalIncome(filteredListBill)
//        tv_pendapatan.text = String.format("Rp %.2f", totalIncome)
//    }
    override fun showBill(listBill: List<Bill>) {
        swipe_layout.isRefreshing = false

        this.listBill.clear()

        // Filter the list to add only items where metode_pembayaran is not null
        val filteredListBill = listBill.filter { it.metodePembayaran != null }
        this.listBill.addAll(filteredListBill)

        historyPaymentAdapter.notifyDataSetChanged()
        view_no_data.visibility = if (filteredListBill.isEmpty()) View.VISIBLE else View.GONE

        // Calculate total income
        val totalIncome = calculateTotalIncome(filteredListBill)

        // Format the total income as currency
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")) as DecimalFormat
        val symbols = formatter.decimalFormatSymbols
        symbols.currencySymbol = "Rp "
        formatter.decimalFormatSymbols = symbols
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0

        val formattedTotalIncome = formatter.format(totalIncome)

        // Display the formatted total income
        tv_pendapatan.text = formattedTotalIncome
    }

    private fun calculateTotalIncome(bills: List<Bill>): Double {
        return bills.filter { it.status == "1" } // Only count paid bills
            .sumOf { bill ->
                stringToDouble(bill.nominal)
            }
    }

    private fun stringToDouble(value: String?): Double {
        return try {
            value?.replace(Regex("[^\\d.]"), "")?.toDoubleOrNull() ?: 0.0
        } catch (e: NumberFormatException) {
            0.0
        }
    }


    private fun initViewRecyclerView(){

        rv_bill.layoutManager = LinearLayoutManager(activity)
        rv_bill.adapter = historyPaymentAdapter

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