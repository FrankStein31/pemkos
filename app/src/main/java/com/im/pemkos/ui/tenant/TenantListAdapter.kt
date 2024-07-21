package com.im.pemkos.ui.tenant

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Tenant
import kotlinx.android.synthetic.main.item_row.view.*

class TenantListAdapter(private val mData: List<Tenant>, private val clickListener: (Tenant) -> Unit): RecyclerView.Adapter<TenantListAdapter.CardViewViewHolder>() {

    private lateinit var ctx: Context
    private  var location: Location? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
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
        holder.itemView.tv_address.text = item.address
        holder.itemView.tv_hp.text = item.contact

        if(item.gender == "L") holder.itemView.iv_placeholder.setImageResource(R.drawable.ic_man_kid)

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