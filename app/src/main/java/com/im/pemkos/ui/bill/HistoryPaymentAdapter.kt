package com.im.pemkos.ui.bill

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import kotlinx.android.synthetic.main.item_history_payment.view.tv_payment_method
import kotlinx.android.synthetic.main.item_row_bill.view.item
import kotlinx.android.synthetic.main.item_row_bill.view.iv_photo
import kotlinx.android.synthetic.main.item_row_bill.view.tv_date
import kotlinx.android.synthetic.main.item_row_bill.view.tv_desc
import kotlinx.android.synthetic.main.item_row_bill.view.tv_nominal
import kotlinx.android.synthetic.main.item_row_bill.view.tv_status
import kotlinx.android.synthetic.main.item_row_bill.view.tv_title

class HistoryPaymentAdapter(private val mData: List<Bill>, private val clickListener: (Bill) -> Unit): RecyclerView.Adapter<HistoryPaymentAdapter.CardViewViewHolder>() {
    private lateinit var ctx: Context
    private lateinit var role: String
    private  var location: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_payment, parent, false)
        ctx = view.context
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = mData[position]
        holder.itemView.tv_title.text = item.name
        holder.itemView.tv_date.text = item.billDate
        holder.itemView.tv_desc.text = "${item.code} ${item.type}"
        holder.itemView.tv_nominal.text = "Rp. ${item.nominal}"
        holder.itemView.tv_status.text = "Status: " + if(item.status == "0")  "Belum bayar" else "Lunas"
        if(item.status == "0"){
            holder.itemView.tv_status.setTextColor(Color.parseColor("#A13842"));
        }

        holder.itemView.tv_payment_method.text = "Pembayaran: ${item.metodePembayaran}"
        if(!item.photo.isNullOrEmpty()){
            Glide
                .with(ctx)
                .load(item.photo)
                .into(holder.itemView.iv_photo)
        }

        holder.itemView.item.setOnClickListener {
            clickListener(item)
        }


    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    }
}