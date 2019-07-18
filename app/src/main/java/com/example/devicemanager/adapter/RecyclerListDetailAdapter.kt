package com.example.devicemanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devicemanager.R
import com.example.devicemanager.activity.DeviceDetailActivity
import java.util.*

class RecyclerListDetailAdapter(internal var context: Context) : RecyclerView.Adapter<RecyclerListDetailAdapter.Holder>() {
    internal var brand: ArrayList<String>? = null
    internal var detail: ArrayList<String> = ArrayList()
    internal var owner: ArrayList<String> = ArrayList()
    internal var addedDate: ArrayList<String> = ArrayList()
    internal var status: ArrayList<String> = ArrayList()
    internal var key: ArrayList<String> = ArrayList()
    internal lateinit var tvBrand: TextView
    internal lateinit var tvDetail: TextView
    internal lateinit var tvOwner: TextView
    internal lateinit var tvAddedDate: TextView
    internal lateinit var tvStatus: TextView

    fun setBrand(brand: ArrayList<String>) {
        this.brand = brand
    }

    fun setKey(key: ArrayList<String>) {
        this.key = key
    }

    fun setDetail(detail: ArrayList<String>) {
        this.detail = detail
    }

    fun setOwner(owner: ArrayList<String>) {
        this.owner = owner
    }

    fun setAddedDate(addedDate: ArrayList<String>) {
        this.addedDate = addedDate
    }

    fun setStatus(status: ArrayList<String>) {
        this.status = status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_detail_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setItem(position)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DeviceDetailActivity::class.java)
            intent.putExtra("serial", key[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return if (brand == null) {
            0
        } else {
            brand!!.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            tvBrand = itemView.findViewById(R.id.tvBrand)
            tvDetail = itemView.findViewById(R.id.tvDetail)
            tvOwner = itemView.findViewById(R.id.tvOwner)
            tvAddedDate = itemView.findViewById(R.id.tvAddedDate)
            tvStatus = itemView.findViewById(R.id.tvStatus)
        }

        @SuppressLint("ResourceAsColor")
        fun setItem(position: Int) {
            tvBrand.text = brand!![position]
            tvDetail.text = detail[position]
            tvOwner.text = owner[position]
            tvAddedDate.text = addedDate[position]
            tvStatus.text = status[position]
            if (status[position].matches("Active".toRegex())) {
                tvStatus.setTextColor(context.resources.getColor(R.color.red))
            } else {
                tvStatus.setTextColor(context.resources.getColor(R.color.green))
            }
        }

    }

}
