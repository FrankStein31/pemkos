package com.im.pemkos.ui.room

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.im.pemkos.R
import com.im.pemkos.model.Room
import kotlinx.android.synthetic.main.item_row.view.*


class RoomSpinnerAdapter(context: Context, resource: Int, list: List<Room>): ArrayAdapter<Room>(context, resource) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_row, parent, false)
        } else {
            view = convertView
        }
        getItem(position)?.let { country ->
            setItemForCountry(parent.context, view, country)
        }

        return view
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        view = layoutInflater.inflate(R.layout.item_row, parent, false)
        getItem(position)?.let { room ->
            setItemForCountry(parent.context, view, room)
        }
//        if (position == 0) {
//            view = layoutInflater.inflate(android.R.layout.simple_selectable_list_item, parent, false)
//            view.setOnClickListener {
//                val root = parent.rootView
//                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
//                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
//            }
//        } else {
//            view = layoutInflater.inflate(R.layout.item_row, parent, false)
//            getItem(position)?.let { room ->
//                setItemForCountry(parent.context, view, room)
//            }
//        }
        return view
    }
    override fun getItem(position: Int): Room? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }
    override fun getCount() = super.getCount() + 1
    override fun isEnabled(position: Int) = position != 0




    private fun setItemForCountry(ctx: Context, view: View, item: Room) {
        view.tv_title.text = item.code+" - "+item.type
        view.tv_address.text = item.facilities  ?: "-"
        view.tv_hp.text = "Rp. "+item.price

        if(item.gender == "L") view.iv_placeholder.setImageResource(R.drawable.ic_man)
        else view.iv_placeholder.setImageResource(R.drawable.ic_women)

        if(!item.photo.isNullOrEmpty()){
            Glide
                .with(ctx)
                .load(item.photo)
                .into(view.iv_photo)
        }
    }

}