package com.example.devicemanager.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devicemanager.R
import com.example.devicemanager.adapter.RecyclerListDetailAdapter
import com.example.devicemanager.manager.Contextor
import com.example.devicemanager.manager.LoadData
import java.util.*


class SummaryListDetailFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var recyclerListDetailAdapter: RecyclerListDetailAdapter? = null
    private var newRecyclerListDetailAdapter: RecyclerListDetailAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var type: String? = null
    private var brandType: String? = null
    private val brand = ArrayList<String>()
    private val detail = ArrayList<String>()
    private val owner = ArrayList<String>()
    private val addedDate = ArrayList<String>()
    private val status = ArrayList<String>()
    private val key = ArrayList<String>()
    private var progressBar: ProgressBar? = null
    private var progressDialogBackground: View? = null
    private var spFilter: Spinner? = null
    private var spSortBy: Spinner? = null
    private var loadData: LoadData? = null
    private val onSpinnerSelect = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
            if (adapterView === spFilter) {
                val filter = adapterView.getItemAtPosition(i).toString()
                when (filter) {
                    "All" -> recyclerView!!.adapter = recyclerListDetailAdapter
                    "Available" -> spinnerSetRecyclerview(filter)
                    "InUse" -> spinnerSetRecyclerview(filter)
                }
            }
            if (adapterView === spSortBy) {
                val sortBy = adapterView.getItemAtPosition(i).toString()
                when (sortBy) {
                    "Date ▲" -> {
                        DownloadData("DateAsc")
                        checkSpType()
                    }
                    "Date ▼" -> {
                        DownloadData("DateDesc")
                        checkSpType()
                    }
                    "Brand ▲" -> {
                        DownloadData("BrandAsc")
                        checkSpType()
                    }
                    "Brand ▼" -> {
                        DownloadData("BrandDesc")
                        checkSpType()
                    }
                }
            }
        }


        override fun onNothingSelected(adapterView: AdapterView<*>) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_summary_list_detail, container, false)
        initInstances(rootView, savedInstanceState)
        return rootView
    }

    private fun init(savedInstanceState: Bundle?) {}

    private fun initInstances(rootView: View, savedInstanceState: Bundle?) {
        type = arguments!!.getString("Type")!!.trim { it <= ' ' }
        brandType = arguments!!.getString("Brand")!!.trim { it <= ' ' }.toLowerCase()

        loadData = LoadData(context!!)
        spFilter = rootView.findViewById(R.id.spinnerFilter)
        spSortBy = rootView.findViewById(R.id.spinnerSortBy)

        val spinnerFilterAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().context!!,
                R.array.filter,
                R.layout.spinner_item_list_detail)
        spinnerFilterAdapter.setDropDownViewResource(R.layout.spinner_item_list_detail)

        val spinnerSortByAdapter = ArrayAdapter.createFromResource(
                Contextor.getInstance().context!!,
                R.array.sort_by,
                R.layout.spinner_item_list_detail)
        spinnerSortByAdapter.setDropDownViewResource(R.layout.spinner_item_list_detail)

        spFilter!!.adapter = spinnerFilterAdapter
        spSortBy!!.adapter = spinnerSortByAdapter

        spFilter!!.onItemSelectedListener = onSpinnerSelect
        spSortBy!!.onItemSelectedListener = onSpinnerSelect

        layoutManager = LinearLayoutManager(activity)

        progressBar = rootView.findViewById(R.id.spin_kit)
        progressDialogBackground = rootView.findViewById(R.id.view)

        progressDialogBackground!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE

        recyclerView = rootView.findViewById(R.id.rvListDetail)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerListDetailAdapter

    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Restore Instance State here
    }

    private fun setSpinnerDefault(spinnerFilterAdapter: ArrayAdapter<CharSequence>, spinner: Spinner) {
        if (spinner === spFilter) {
            val spinnerPosition = spinnerFilterAdapter.getPosition("All")
            spinner.setSelection(spinnerPosition)
        } else if (spinner === spSortBy) {
            val spinnerPosition = spinnerFilterAdapter.getPosition("Date")
            spinner.setSelection(spinnerPosition)
        }
    }

    private fun DownloadData(order: String) {
        brand.clear()
        detail.clear()
        owner.clear()
        addedDate.clear()
        brand.clear()
        key.clear()
        status.clear()

        recyclerListDetailAdapter = RecyclerListDetailAdapter(context!!)

        val itemEntities = loadData!!.selectProductByType(type!!, order)
        if (itemEntities != null) {
            for (i in itemEntities.indices) {
                if (brandType!!.matches("-".toRegex()) || brandType!!.matches(itemEntities[i].brand!!.trim { it <= ' ' }.toLowerCase().toRegex())) {
                    val productType = itemEntities[i].type!!.trim { it <= ' ' }
                    val productBrand = itemEntities[i].brand!!.trim { it <= ' ' }
                    val productDetail = itemEntities[i].detail!!.trim { it <= ' ' }
                    val productAddedDate = itemEntities[i].purchasedDate!!.trim { it <= ' ' }
                    val productOwner = itemEntities[i].placeName!!.trim { it <= ' ' }
                    val productStatus: String
                    if (productOwner.matches("-".toRegex())) {
                        productStatus = "Available"
                    } else {
                        productStatus = "InUse"
                    }
                    val productKey = itemEntities[i].unnamed2!!.trim { it <= ' ' }
                    Log.d("date", "" + productAddedDate)
                    brand.add(productBrand)
                    detail.add(productDetail)
                    owner.add(productOwner)
                    addedDate.add(productAddedDate)
                    status.add(productStatus)
                    key.add(productKey)
                }
            }
        }
        recyclerListDetailAdapter!!.setBrand(brand)
        recyclerListDetailAdapter!!.setDetail(detail)
        recyclerListDetailAdapter!!.setOwner(owner)
        recyclerListDetailAdapter!!.setAddedDate(addedDate)
        recyclerListDetailAdapter!!.setStatus(status)
        recyclerListDetailAdapter!!.setKey(key)
        recyclerListDetailAdapter!!.notifyDataSetChanged()

        progressDialogBackground!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.INVISIBLE
    }

    private fun checkSpType() {
        val filter = spFilter!!.selectedItem.toString()
        if (!filter.matches("All".toRegex())) {
            spinnerSetRecyclerview(filter)
        } else {
            recyclerView!!.layoutManager = layoutManager
            recyclerView!!.adapter = recyclerListDetailAdapter
        }
    }

    private fun spinnerSetRecyclerview(spinnerStatus: String) {

        val filterStatus = ArrayList<String>()
        val filterBrand = ArrayList<String>()
        val filterDetail = ArrayList<String>()
        val filterOwner = ArrayList<String>()
        val filterAddedDate = ArrayList<String>()
        val filterKey = ArrayList<String>()
        for (position in status.indices) {
            if (status[position].matches(spinnerStatus.toRegex())) {
                filterStatus.add(status[position])
                filterBrand.add(brand[position])
                filterDetail.add(detail[position])
                filterOwner.add(owner[position])
                filterAddedDate.add(addedDate[position])
                filterKey.add(key[position])
            }
        }
        newRecyclerListDetailAdapter = RecyclerListDetailAdapter(context!!)
        newRecyclerListDetailAdapter!!.setBrand(filterBrand)
        newRecyclerListDetailAdapter!!.setDetail(filterDetail)
        newRecyclerListDetailAdapter!!.setOwner(filterOwner)
        newRecyclerListDetailAdapter!!.setAddedDate(filterAddedDate)
        newRecyclerListDetailAdapter!!.setStatus(filterStatus)
        newRecyclerListDetailAdapter!!.setKey(filterKey)
        newRecyclerListDetailAdapter!!.notifyDataSetChanged()
        recyclerView!!.adapter = newRecyclerListDetailAdapter
    }

    companion object {

        fun newInstance(): SummaryListDetailFragment {
            val fragment = SummaryListDetailFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
