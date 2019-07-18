package com.example.devicemanager.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devicemanager.R
import com.example.devicemanager.adapter.SummaryAdapter
import com.example.devicemanager.manager.LoadData
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SummaryFragment : Fragment() {
    internal var typeDevice: Array<String>
    internal var typeFurniture: Array<String>
    internal var typeOther: Array<String>
    internal var typeAll: Array<String>
    internal var intDevice: IntArray? = null
    internal var intFurniture: IntArray? = null
    internal var intOther: IntArray? = null
    internal var intAll: IntArray? = null
    private var fabContainer: FloatingActionButton? = null
    private var isFABOpen = false
    private var rvSummary: RecyclerView? = null
    private var summaryAdapter: SummaryAdapter? = null
    private var summaryAdapterLaptop: SummaryAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var layoutAll: RelativeLayout? = null
    private var layoutDevice: RelativeLayout? = null
    private var layoutLaptop: RelativeLayout? = null
    private var layoutFurniture: RelativeLayout? = null
    private var layoutOther: RelativeLayout? = null
    private var hidedView: View? = null
    private var loadData: LoadData? = null
    private val onClickView = View.OnClickListener { closeFABMenu() }
    private val onClickListener = View.OnClickListener { view ->
        if (view === fabContainer) {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        } else if (view === layoutAll) {
            getDataByType(typeAll)
            closeFABMenu()
        } else if (view === layoutDevice) {
            getDataByType(typeDevice)
            closeFABMenu()
        } else if (view === layoutLaptop) {
            getLaptop()
            closeFABMenu()
        } else if (view === layoutFurniture) {
            getDataByType(typeFurniture)
            closeFABMenu()
        } else if (view === layoutOther) {
            getDataByType(typeOther)
            closeFABMenu()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_summary, container, false)
        initInstances(rootView, savedInstanceState)
        return rootView
    }

    private fun initInstances(rootView: View, savedInstanceState: Bundle?) {
        loadData = LoadData(context!!)
        fabContainer = rootView.findViewById(R.id.fabFilter)

        layoutAll = rootView.findViewById(R.id.layoutAll)
        layoutDevice = rootView.findViewById(R.id.layoutDevice)
        layoutFurniture = rootView.findViewById(R.id.layoutFurniture)
        layoutLaptop = rootView.findViewById(R.id.layoutLaptop)
        layoutOther = rootView.findViewById(R.id.layoutOther)
        hidedView = rootView.findViewById(R.id.hidedView)

        fabContainer!!.setOnClickListener(onClickListener)
        layoutAll!!.setOnClickListener(onClickListener)
        layoutOther!!.setOnClickListener(onClickListener)
        layoutDevice!!.setOnClickListener(onClickListener)
        layoutLaptop!!.setOnClickListener(onClickListener)
        layoutFurniture!!.setOnClickListener(onClickListener)

        layoutManager = LinearLayoutManager(context)
        rvSummary = rootView.findViewById(R.id.rvSummary)

        typeAll = resources.getStringArray(R.array.allType)
        //                new String[]{"ACCESS POINT", "ADAPTER", "AIR CONDITIONER", "APPLE CARE", "BARCODE READER", "BATTERY",
        //                "BICYCLE", "CABINET", "CAMERA", "CAR", "CARD READER", "CARPET", "CART", "CASH DRAWER", "CHAIR",
        //                "CHARGER", "CHROMECAST", "CLOTHES DRYERS", "COFFEE MACHINE", "COMPUTER", "COUNTER", "CURTAIN",
        //                "DEVELOPER PROGRAM", "DISPLAY PORT", "DOCUMENT SHREDDER", "DONGLE", "DOOR ACCESS", "DRAWER",
        //                "E-COMMERCE", "EQUIPMENT", "FAN", "FILM", "FURNITURE", "GAME", "GAS STOVE", "HDD",
        //                "IMAC", "INTERIOR DECORATION", "IPAD", "IPAD COVER", "IPOD", "ITEM", "JUICE BLENDER",
        //                "KEYBOARD", "KITCHEN", "LABEL PRINTER", "LAMP", "LAPTOP", "LOCKER", "MICRO SD CARD",
        //                "MINIDRIVE", "MIRCROWAVE", "MOBILE PHONE", "MODEM ROUTER", "MONITOR", "NETWORK SWITCH",
        //                "POCKET WIFI", "POWER BANK", "POWER SUPPLIER", "PRINTER", "PROGRAM", "REFRIGERATOR",
        //                "RICE COOKER", "ROUTER", "SCANNER", "SERVER", "SERVER CABINET", "SHELVES", "SINK",
        //                "SOFA", "SOFTWARE", "SOLID STATE DRIVE ", "SSD", "STOOL", "SWING", "TABLE", "TABLET",
        //                "TELEPHONE", "USB", "WASHING MACHINE", "WATCH", "WATER HEATER", "WATER PUMP",
        //                "WHITE BOARD", "WIRELESS", "ขาแขวน", "อุปกรณ์คอมพิวเตอร์"};

        typeDevice = resources.getStringArray(R.array.device_and_accessory)
        //                new String[]{"ACCESS POINT", "ADAPTER", "APPLE CARE", "BATTERY", "CARD READER",
        //                "CHARGER", "CHROMECAST", "COMPUTER", "DEVELOPER PROGRAM", "DISPLAY PORT", "DONGLE", "E-COMMERCE",
        //                "GAME", "HDD", "IMAC", "IPAD", "IPAD COVER", "IPOD", "ITEM", "KEYBOARD", "LAPTOP", "MICRO SD CARD",
        //                "MINIDRIVE", "MOBILE PHONE", "MODEM ROUTER", "MONITOR", "NETWORK SWITCH", "POCKET WIFI", "POWER BANK",
        //                "POWER SUPPLIER", "PROGRAM", "ROUTER", "SERVER", "SERVER CABINET", "SOFTWARE", "SOLID STATE DRIVE ",
        //                "SSD", "TABLET", "USB", "WIRELESS", "ขาแขวน", "อุปกรณ์คอมพิวเตอร์"};

        typeFurniture = resources.getStringArray(R.array.furniture)
        //                new String[]{"AIR CONDITIONER", "BARCODE READER", "CABINET", "CAMERA", "CARPET", "CART", "CASH DRAWER",
        //                "CHAIR", "CLOTHES DRYERS", "COFFEE MACHINE", "COUNTER", "CURTAIN", "DOCUMENT SHREDDER", "DOOR ACCESS", "DRAWER",
        //                "EQUIPMENT", "FAN", "FILM", "FURNITURE", "GAS STOVE", "INTERIOR DECORATION", "JUICE BLENDER", "KITCHEN",
        //                "LABEL PRINTER", "LAMP", "LOCKER", "MIRCROWAVE", "PRINTER", "REFRIGERATOR", "RICE COOKER", "SCANNER", "SHELVES",
        //                "SINK", "SOFA", "STOOL", "SWING", "TABLE", "TABLET", "TELEPHONE", "TELEVISION", "WASHING MACHINE", "WATCH",
        //                "WATER HEATER", "WATER PUMP", "WHITE BOARD"};

        typeOther = resources.getStringArray(R.array.other_summary)
        //                new String[]{"BUILDING", "BICYCLE", "CAR"};

        getDataByType(typeAll)

    }

    private fun getDataByType(type: Array<String>?) {
        if (type == null || type.size == 0) {
            Toast.makeText(activity, "Error to get ", Toast.LENGTH_SHORT).show()
            return
        }
        summaryAdapter = SummaryAdapter(context!!)
        val typeTotal = IntArray(type.size)
        val typeAvailable = IntArray(type.size)
        val typeInUse = IntArray(type.size)
        val itemEntities = loadData!!.item

        for (i in itemEntities.indices) {
            for (j in type.indices)
                if (itemEntities[i].type!!.trim { it <= ' ' }.matches(type[j].toRegex())) {
                    val place = itemEntities[i].placeName
                    if (place!!.matches("-".toRegex())) {
                        typeAvailable[j] = typeAvailable[j] + 1
                    } else {
                        typeInUse[j] = typeInUse[j] + 1
                    }
                    typeTotal[j] = typeTotal[j] + 1
                    break
                }
        }
        summaryAdapter!!.setAvailable(typeAvailable)
        summaryAdapter!!.setBrand(null!!)
        summaryAdapter!!.setType(type)
        summaryAdapter!!.setCount(typeInUse)
        summaryAdapter!!.setTotal(typeTotal)
        summaryAdapter!!.notifyDataSetChanged()
        rvSummary!!.layoutManager = layoutManager
        rvSummary!!.adapter = summaryAdapter
    }

    private fun getLaptop() {
        summaryAdapterLaptop = SummaryAdapter(context!!)
        val type = arrayOf("LAPTOP")
        val brand = arrayOf("Apple", "Dell", "HP", "Lenovo", "True IDC Chromebook 11", "-")
        val brandTotal = IntArray(brand.size)
        val brandAvailable = IntArray(brand.size)
        val brandInUse = IntArray(brand.size)
        val itemEntities = loadData!!.item

        for (i in itemEntities.indices) {
            for (j in brand.indices)
                if (itemEntities[i].type!!.trim { it <= ' ' }.toLowerCase().matches(type[0].toLowerCase().toRegex())) {
                    if (itemEntities[i].brand!!.trim { it <= ' ' }.toLowerCase().matches(brand[j].trim { it <= ' ' }.toLowerCase().toRegex())) {
                        val place = itemEntities[i].placeName
                        if (place!!.matches("-".toRegex())) {
                            brandAvailable[j] = brandAvailable[j] + 1
                        } else {
                            brandInUse[j] = brandInUse[j] + 1
                        }
                        brandTotal[j] = brandTotal[j] + 1
                        break
                    }
                }
        }
        summaryAdapterLaptop!!.setAvailable(brandAvailable)
        summaryAdapterLaptop!!.setBrand(brand)
        summaryAdapterLaptop!!.setType(type)
        summaryAdapterLaptop!!.setCount(brandInUse)
        summaryAdapterLaptop!!.setTotal(brandTotal)
        summaryAdapterLaptop!!.notifyDataSetChanged()
        rvSummary!!.layoutManager = layoutManager
        rvSummary!!.adapter = summaryAdapterLaptop
    }

    private fun closeFABMenu() {
        isFABOpen = false
        hidedView!!.visibility = View.INVISIBLE
        layoutAll!!.animate().translationY(0f)
        layoutLaptop!!.animate().translationY(0f)
        layoutDevice!!.animate().translationY(0f)
        layoutFurniture!!.animate().translationY(0f)
        layoutOther!!.animate().translationY(0f)
        delayCloseFab()
    }

    private fun showFABMenu() {
        isFABOpen = true
        delayOpenFab()
        hidedView!!.visibility = View.VISIBLE
        hidedView!!.setOnClickListener(onClickView)
        layoutAll!!.animate().translationY(-resources.getDimension(R.dimen.transition_floating_1))
        layoutLaptop!!.animate().translationY(-resources.getDimension(R.dimen.transition_floating_2))
        layoutDevice!!.animate().translationY(-resources.getDimension(R.dimen.transition_floating_3))
        layoutFurniture!!.animate().translationY(-resources.getDimension(R.dimen.transition_floating_4))
        layoutOther!!.animate().translationY(-resources.getDimension(R.dimen.transition_floating_5))
    }

    private fun delayCloseFab() {
        val handler = Handler()
        handler.postDelayed({
            layoutAll!!.visibility = View.INVISIBLE
            layoutDevice!!.visibility = View.INVISIBLE
            layoutLaptop!!.visibility = View.INVISIBLE
            layoutFurniture!!.visibility = View.INVISIBLE
            layoutOther!!.visibility = View.INVISIBLE
        }, 200)
    }

    private fun delayOpenFab() {
        val handler = Handler()
        handler.postDelayed({
            layoutAll!!.visibility = View.VISIBLE
            layoutLaptop!!.visibility = View.VISIBLE
            layoutDevice!!.visibility = View.VISIBLE
            layoutFurniture!!.visibility = View.VISIBLE
            layoutOther!!.visibility = View.VISIBLE
        }, 200)
    }

    companion object {

        fun newInstance(): SummaryFragment {
            val fragment = SummaryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
