package com.im.pemkos.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.network.response.Bill
import com.im.pemkos.network.response.BillUser
import kotlinx.android.synthetic.main.item_riwayat_tagihan.view.*

class RiwayatTagihanAdapter(private var mData: List<Bill>, private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RiwayatTagihanAdapter.CardViewViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(billUser: Bill?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat_tagihan, parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item: Bill = mData[position]
        holder.itemView.tv_date_riwayat.text = item.billDate
        holder.itemView.tv_desc_riwayat.text = "${item.code} ${item.type}"
        holder.itemView.tv_nominal_riwayat.text = "Rp. ${item.nominal}"
        holder.itemView.tv_status_riwayat.text =
            "Status: " + if (item.status == "0") "Belum bayar" else "Lunas"
        if (item.status == "0") {
            holder.itemView.tv_status_riwayat.setTextColor(Color.parseColor("#A13842"))
        }
        holder.itemView.tv_payment_method_riwayat.text = "Pembayaran: ${item.metodePembayaran}"
        if (!item.photo.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load(item.photo).into(holder.itemView.iv_photo)
        }
        holder.itemView.item.setOnClickListener { clickListener.onItemClick(item) }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateData(newData: List<Bill>) {
        mData = newData
        notifyDataSetChanged()
    }
}