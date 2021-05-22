package com.sainivik.backgoundlivelocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.databinding.ItemLocationLogsBinding
import com.sainivik.backgoundlivelocation.interfaces.RecyclerClickListener
import com.sainivik.backgoundlivelocation.model.LocationTable
import com.sainivik.backgoundlivelocation.util.MiscUtil

class LocationLogsAdapter(


    private var list: ArrayList<LocationTable>,
    private var listener: RecyclerClickListener
) : RecyclerView.Adapter<LocationLogsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DataBindingUtil.inflate<ItemLocationLogsBinding>(
            LayoutInflater.from(parent.context), R.layout.item_location_logs, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.model = list[position]
        holder.binding.startTime = MiscUtil.getDate(list[position].startTime)
        holder.binding.endTime = MiscUtil.getDate(list[position].stopTime)
        holder.binding.llMain.setOnClickListener {
            listener.click(holder.binding.llMain, position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(private val mBinding: ItemLocationLogsBinding) :
        ViewHolder(mBinding.root) {
        val binding = mBinding

    }


}