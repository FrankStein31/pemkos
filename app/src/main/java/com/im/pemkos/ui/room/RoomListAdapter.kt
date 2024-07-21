package com.im.pemkos.ui.room

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Room
import kotlinx.android.synthetic.main.item_row_room.view.*


class RoomListAdapter(private val mData: List<Room>, private val clickListener: (Room) -> Unit): RecyclerView.Adapter<RoomListAdapter.CardViewViewHolder>() {

    private lateinit var ctx: Context
    private  var location: Location? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_room, parent, false)
        ctx = view.context
        return CardViewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = mData[position]
        holder.itemView.tv_title.text = item.code
        holder.itemView.tv_label.text = item.kostName
        holder.itemView.tv_address.text = item.type
        holder.itemView.tv_hp.text = item.facilities+"\n"+item.gender
        if(item.statusKamar == "0"){
            holder.itemView.tv_status_room.text = "Kamar: Tersedia"

        }else{
//            holder.itemView.tv_status_room.text = "Kamar: Terisi || Penghuni: " + item.namapenghuni
//            holder.itemView.tv_status_room.setTextColor(Color.parseColor("#A13842"));
            val sb = SpannableStringBuilder()
            val kamarTerisi = "Kamar: Terisi"
            val spanKamarTerisi = SpannableString(kamarTerisi)
            spanKamarTerisi.setSpan(
                ForegroundColorSpan(Color.parseColor("#A13842")),
                0,
                kamarTerisi.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sb.append(spanKamarTerisi)
            val namaPenghuni: String = " || Penghuni: " + item.namapenghuni
            val spanNamaPenghuni = SpannableString(namaPenghuni)
            spanNamaPenghuni.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                namaPenghuni.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sb.append(spanNamaPenghuni)
            holder.itemView.tv_status_room.text = sb


        }

        Log.e("statuskamar", "status kamar = "  + item.statusKamar)

        if(item.gender == "L") holder.itemView.iv_placeholder.setImageResource(R.drawable.ic_man)
        else holder.itemView.iv_placeholder.setImageResource(R.drawable.ic_women)

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