package com.example.devicemanager.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.devicemanager.R
import com.example.devicemanager.activity.AddDeviceActivity
import com.example.devicemanager.activity.DeviceDetailActivity
import com.example.devicemanager.activity.ScanBarcodeActivity
import com.example.devicemanager.adapter.ItemListAdapter
import com.example.devicemanager.manager.DataManager
import com.example.devicemanager.manager.LoadData
import com.example.devicemanager.room.AppDatabase
import com.example.devicemanager.room.ItemEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class MainFragment : Fragment(), ItemListAdapter.Holder.ItemClickListener {

    internal var sp: SharedPreferences
    internal var editor: SharedPreferences.Editor
    internal var database: AppDatabase? = null
    private val btnAdd: Button? = null
    private val btnCheck: Button? = null
    private val btnSummary: Button? = null
    private var floatingButton: FloatingActionButton? = null
    private val searchView: SearchView? = null
    private val isFABOpen = false
    private val tvLogout: TextView? = null
    private val mAuth: FirebaseAuth? = null
    private var recyclerView: RecyclerView? = null
    private var dataManager: DataManager? = null
    private var adapter: ItemListAdapter? = null
    private val adapterNew: ItemListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var loadData: LoadData? = null
    private val downloadStatus: Boolean? = null
    private var view: View? = null
    private var progressBar: ProgressBar? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    internal var pullToRefresh: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        progressBar!!.visibility = View.VISIBLE
        view!!.visibility = View.VISIBLE
        swipeRefreshLayout!!.isRefreshing = false
        if (loadData!!.deleteTable() == 1) {
            loadData()
            Log.d("test1607", "swipe")
        }
    }
    private val onClickFab = View.OnClickListener {
        val intent = Intent(activity, AddDeviceActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        initInstances(rootView, savedInstanceState)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        MenuItemCompat.expandActionView(menuItem)

        val searchViewActionBar = MenuItemCompat.getActionView(menuItem) as SearchView
        searchViewActionBar.clearFocus()
        searchViewActionBar.setIconifiedByDefault(false)
        searchViewActionBar.setPadding(0, 0, 20, 0)
        searchViewActionBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                view!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.VISIBLE

                adapter!!.filter.filter(query)

                searchViewActionBar.clearFocus()
                view!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.INVISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return true
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_scan) {
            val intent = Intent(activity, ScanBarcodeActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    override fun onItemClick(view: View, position: Int, serial: String?) {
        val intent = Intent(activity, DeviceDetailActivity::class.java)
        intent.putExtra("serial", serial)
        startActivity(intent)
    }

    private fun init(savedInstanceState: Bundle?) {
        // Init Fragment level's variable(s) here
    }

    private fun initInstances(rootView: View, savedInstanceState: Bundle?) {
        sp = context!!.getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE)
        editor = sp.edit()

        floatingButton = rootView.findViewById(R.id.fabAdd)
        floatingButton!!.setOnClickListener(onClickFab)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        view = rootView.findViewById(R.id.view)
        progressBar = rootView.findViewById(R.id.spin_kit)

        dataManager = DataManager()
        loadData = LoadData(context!!)

        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        adapter = ItemListAdapter(context!!)
        adapter!!.setClickListener(this)
        recyclerView!!.adapter = adapter
        adapter!!.notifyDataSetChanged()
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout!!.setOnRefreshListener(pullToRefresh)

    }

    private fun loadData() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Data")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (s in dataSnapshot.children) {
                    val item = s.getValue(ItemEntity::class.java)

                    if (item != null) {
                        if (!item.purchasedDate!!.matches("".toRegex()) && !item.purchasedDate!!.matches("-".toRegex())) {
                            item.purchasedDate = setDate(item.purchasedDate!!)
                        }
                        item.autoId = Integer.parseInt(s.key)
                        loadData!!.insert(item)
                    }
                }
                ItemListAdapter(context!!)
                recyclerView!!.adapter = adapter

                view!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.INVISIBLE
                swipeRefreshLayout!!.isRefreshing = false
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun setDate(inputDate: String): String {
        var inputDate = inputDate
        if (inputDate.contains("GMT")) {
            inputDate = inputDate.substring(0, inputDate.indexOf("GMT")).trim { it <= ' ' }
        }
        val inputFormat = "EEE MMM dd yyyy HH:mm:ss"
        val inputDateFormat = SimpleDateFormat(
                inputFormat, Locale.ENGLISH)
        val outputFormat = "yyyy-MM-dd"
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

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
