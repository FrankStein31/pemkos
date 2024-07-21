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
import kotlinx.android.synthetic.main.item_row_bill.view.*

class BillListAdapter(private val mData: List<Bill>, private val clickListener: (Bill) -> Unit): RecyclerView.Adapter<BillListAdapter.CardViewViewHolder>() {

    private lateinit var ctx: Context
    private lateinit var role: String
    private  var location: Location? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_bill, parent, false)
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
        holder.itemView.tv_status.text = if(item.status == "0")  "Belum bayar" else "Lunas"
        if(item.status == "0"){
            holder.itemView.tv_status.setTextColor(Color.parseColor("#A13842"));
        }
        if(!item.photo.isNullOrEmpty()){
            Glide
                    .with(ctx)
                    .load(item.photo)
                    .into(holder.itemView.iv_photo)
        }
// menyambungkan dengan wa//
        holder.itemView.iv_chat.setOnClickListener {
            var hp = if(item.contact.substring(0, 2) == "08") "62${item.contact.substring(1)}" else item.contact

            var date = item.billDate.split("-")
            val msg: String = "Hi ${item.name}, anda belum melakukan pembayaran tagihan kos bulan ${date[1].toInt()} tahun ${date[0]} \"Admin Kos BU TIK\""
            val url = "https://api.whatsapp.com/send?phone=$hp&text=$msg"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            ctx.startActivity(i)
        }

        holder.itemView.item.setOnClickListener {
            clickListener(item)
        }


    }


    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    }
}