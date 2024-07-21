package com.im.pemkos.ui.kost

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.im.pemkos.R
import com.im.pemkos.model.Kost
import kotlinx.android.synthetic.main.item_row_kos.view.*

class KostListAdapter(private val mData: List<Kost>, private val clickListener: (Kost) -> Unit): RecyclerView.Adapter<KostListAdapter.CardViewViewHolder>() {

    private lateinit var ctx: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_kos, parent, false)
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

        holder.itemView.btn_delete.setOnClickListener {
            clickListener(item)
        }
    }


    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}