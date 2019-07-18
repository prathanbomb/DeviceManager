package com.example.devicemanager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.devicemanager.R
import com.example.devicemanager.activity.SummaryListDetailActivity
import com.example.devicemanager.manager.Contextor

class SummaryAdapter(private val context: Context) : RecyclerView.Adapter<SummaryAdapter.Holder>() {
    private var available: IntArray? = null
    private var count: IntArray? = null
    private var total: IntArray? = null
    private val contector = Contextor.getInstance().context
    private var brand: Array<String>? = null
    private var type: Array<String>? = null
    private val funiture = contector!!.resources.getStringArray(R.array.furniture)
    private val other = contector!!.resources.getStringArray(R.array.other_summary)

    fun setBrand(brand: Array<String>) {
        this.brand = brand
    }

    fun setTotal(total: IntArray) {
        this.total = total
    }

    fun setCount(count: IntArray) {
        this.count = count
    }

    fun setAvailable(available: IntArray) {
        this.available = available
    }

    fun setType(type: Array<String>) {
        this.type = type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_other_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setItem(position)
        holder.itemView.setOnClickListener {
            if (type!!.size > 1) {
                val intent = Intent(context, SummaryListDetailActivity::class.java)
                intent.putExtra("Type", type!![position])
                intent.putExtra("Brand", "-")
                context.startActivity(intent)
            } else {
                val intent = Intent(context, SummaryListDetailActivity::class.java)
                intent.putExtra("Type", type!![0])
                intent.putExtra("Brand", brand!![position])
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        if (type == null)
            return 0
        return if (type!!.size == 1) {
            brand!!.size
        } else type!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvBrand: TextView
        var tvTotal: TextView
        var tvAvailable: TextView
        var tvActive: TextView
        var tvType: TextView
        var imgView: ImageView

        init {
            tvBrand = itemView.findViewById(R.id.tvBrand)
            tvTotal = itemView.findViewById(R.id.tvTotal)
            tvAvailable = itemView.findViewById(R.id.tvAvailable)
            tvActive = itemView.findViewById(R.id.tvActive)
            tvType = itemView.findViewById(R.id.tvType)
            imgView = itemView.findViewById(R.id.imgView)
        }

        fun setItem(position: Int) {
            if (brand != null) {
                tvBrand.text = "" + brand!![position]
                when (brand!![position].toLowerCase()) {
                    "apple" -> imgView.setImageResource(R.drawable.ic_brand_apple)
                    "hp" -> imgView.setImageResource(R.drawable.ic_brand_hp)
                    "lenovo" -> imgView.setImageResource(R.drawable.ic_brand_lenovo)
                    "dell" -> imgView.setImageResource(R.drawable.ic_brand_dell)
                    "true idc chromebook 11" -> imgView.setImageResource(R.drawable.ic_brand_chromebook)
                    "-" -> imgView.setImageResource(R.drawable.ic_barcode)
                }
            }
            tvTotal.text = "" + total!![position]
            tvAvailable.text = "" + available!![position]
            tvActive.text = "" + count!![position]
            if (type!!.size <= 1) {
                tvType.text = type!![0]
                setImage(0)
            } else {
                tvType.text = "" + type!![position]
                setImage(position)
            }
        }

        private fun setImage(position: Int) {
            if (brand != null) {
                return
            }
            if (type!![position].matches("LAPTOP".toRegex())) {
                imgView.setImageResource(R.drawable.ic_summary_laptop)
                return
            }
            for (i in other.indices) {
                if (other[i].toUpperCase().trim { it <= ' ' }.matches(type!![position].toUpperCase().trim { it <= ' ' }.toRegex())) {
                    imgView.setImageResource(R.drawable.ic_summary_other)
                    return
                }
            }
            for (i in funiture.indices) {
                if (funiture[i].toUpperCase().trim { it <= ' ' }.matches(type!![position].toUpperCase().trim { it <= ' ' }.toRegex())) {
                    imgView.setImageResource(R.drawable.ic_summary_furniture_s)
                    return
                }
            }
            imgView.setImageResource(R.drawable.ic_summary_device_s)

        }
    }
}
