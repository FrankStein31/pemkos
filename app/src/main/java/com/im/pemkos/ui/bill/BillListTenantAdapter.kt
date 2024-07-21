package com.im.pemkos.ui.bill

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import kotlinx.android.synthetic.main.item_row_bill_tenant.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate

class BillListTenantAdapter(private val mData: List<Bill>, private val clickListener: (Bill) -> Unit): RecyclerView.Adapter<BillListTenantAdapter.CardViewViewHolder>() {

    private lateinit var ctx: Context
    private lateinit var role: String
    private  var location: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_bill_tenant, parent, false)
        ctx = view.context
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
//        return mData.size
        return mData.filter { it.metodePembayaran == null }.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.setIsRecyclable(false)
//        val item = mData[position]

        val filteredData = mData.filter { it.metodePembayaran == null }
        val item = filteredData[position]

        // Hanya menampilkan item jika metode_pembayaran null
//        if (item.metodePembayaran == null) {
            val date = SimpleDateFormat("yyyy-MM-dd").parse(item.billDate)

            holder.itemView.tv_date.text = SimpleDateFormat("dd MMM yyyy").format(date)
            holder.itemView.tv_desc.text = "Kamar kos : ${item.code} ${item.type}"
            holder.itemView.tv_nominal.text = "Rp. ${item.nominal}"
            holder.itemView.tv_status.text = if(item.status == "0")  "Belum bayar" else "Lunas"
            if(item.status == "0"){
                holder.itemView.tv_status.setTextColor(Color.parseColor("#A13842"));
            }

            holder.itemView.iv_chat.visibility = View.GONE

            holder.itemView.item.setOnClickListener {
                clickListener(item)
            }
//            holder.itemView.visibility = View.VISIBLE
//        } else {
//            // Menyembunyikan item jika metode_pembayaran tidak null
//            holder.itemView.visibility = View.GONE
//        }
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
}