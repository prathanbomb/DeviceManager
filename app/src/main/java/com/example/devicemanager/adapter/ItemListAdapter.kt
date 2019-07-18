package com.example.devicemanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devicemanager.R
import com.example.devicemanager.manager.Contextor
import com.example.devicemanager.manager.LoadData
import com.example.devicemanager.room.ItemEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ItemListAdapter(private val context: Context) : RecyclerView.Adapter<ItemListAdapter.Holder>() {
    private var mClickListener: Holder.ItemClickListener? = null
    private val loadData: LoadData = LoadData(context)
    private val filteredList = ArrayList<ItemEntity>()
    var itemId: String? = null
    val filter: Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
            filteredList.clear()
            var checkData = false
            if (charSequence == null || charSequence.length == 0) {
                filteredList.addAll(loadData.orderedItem)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                for (i in source.indices) {
                    val brand = source[i].brand
                    val type = source[i].type
                    val detail = source[i].detail
                    val date = source[i].purchasedDate
                    val place = source[i].placeName

                    val data = brand + type + detail + date + place

                    for (s in filterPattern) {
                        checkData = data.toLowerCase().trim { it <= ' ' }.contains(s)
                        if (!checkData) {
                            break
                        }
                    }
                    if (checkData) {
                        filteredList.add(source[i])
                    }
                }
            }
            val results = Filter.FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
            list.clear()
            val posts: List<ItemEntity>?
            posts = filterResults.values as List<ItemEntity>
            if (posts != null) {
                list.addAll(posts)
            }
            notifyDataSetChanged()
        }
    }

    init {
        list = loadData.orderedItem.toMutableList()
        source = ArrayList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListAdapter.Holder {
        val view = LayoutInflater.from(Contextor.getInstance().context)
                .inflate(R.layout.list_item_search, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (itemCount > 0 && list[position] != null) {
            holder.setText(position)
        }
        holder.itemView.setOnClickListener { v ->
            if (mClickListener != null) {
                mClickListener!!.onItemClick(v, position, list[position].unnamed2)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setClickListener(itemClickListener: Holder.ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    class Holder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSearchType: TextView
        private val tvSearchDetail: TextView
        private val tvSearchName: TextView
        private val tvSearchPurchasedDate: TextView

        init {
            tvSearchType = itemView.findViewById(R.id.tvSearchType)
            tvSearchDetail = itemView.findViewById(R.id.tvSearchDetail)
            tvSearchName = itemView.findViewById(R.id.tvSearchName)
            tvSearchPurchasedDate = itemView.findViewById(R.id.tvSearchPurchasedDate)
        }

        @SuppressLint("SetTextI18n")
        fun setText(position: Int) {
            val brand = list[position].brand!!.trim { it <= ' ' }
            val detail = list[position].detail!!.trim { it <= ' ' }

            if (!brand.matches("-".toRegex()) && !checkBrand(detail, brand)) {
                tvSearchDetail.text = "($brand) $detail"
            } else {
                tvSearchDetail.text = detail
            }
            tvSearchName.text = list[position].placeName!!.trim { it <= ' ' }
            tvSearchType.text = list[position].type!!.trim { it <= ' ' }
            tvSearchPurchasedDate.text = setDate(list[position].purchasedDate!!.trim { it <= ' ' })
        }

        private fun checkBrand(detail: String, brand: String): Boolean {
            return detail.toLowerCase().contains(brand.toLowerCase())
        }

        private fun setDate(inputDate: String): String {

            val inputFormat = "yyyy-MM-dd"
            val inputDateFormat = SimpleDateFormat(
                    inputFormat, Locale.ENGLISH)
            val outputFormat = "dd/MM/yyyy"
            val outputDateFormat = SimpleDateFormat(
                    outputFormat, Locale.ENGLISH)

            val date: Date?
            var str = inputDate

            try {
                date = inputDateFormat.parse(inputDate)
                str = outputDateFormat.format(date!!)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str

        }

        interface ItemClickListener {
            fun onItemClick(view: View, position: Int, serial: String?)
        }
    }

    companion object {

        private lateinit var source: ArrayList<ItemEntity>
        private lateinit var list: MutableList<ItemEntity>

        fun getList(): List<ItemEntity> {
            return list
        }
    }
}
