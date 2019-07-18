package com.example.devicemanager.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.example.devicemanager.R
import com.example.devicemanager.activity.ScanBarCodeAddDeviceActivity
import com.example.devicemanager.manager.LoadData
import com.example.devicemanager.model.DataItem
import com.example.devicemanager.room.ItemEntity
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddDeviceFragment : Fragment() {

    internal var itemEntity: List<ItemEntity>? = null
    internal var sp: SharedPreferences
    internal var editor: SharedPreferences.Editor
    private var spType: Spinner? = null
    private var spTypeList: Spinner? = null
    private var spBranch: Spinner? = null
    private var etOwnerName: EditText? = null
    private var etSerialNumber: EditText? = null
    private var etDeviceDetail: EditText? = null
    private var etDatePicker: EditText? = null
    private var etOwnerId: EditText? = null
    private var etBrand: EditText? = null
    private var etDeviceModel: EditText? = null
    private var etDevicePrice: EditText? = null
    private var etNote: EditText? = null
    private var etQuantity: EditText? = null
    private var etPurchasePrice: EditText? = null
    private var etForwardDepreciation: EditText? = null
    private var etDepreciationRate: EditText? = null
    private var etDepreciationinYear: EditText? = null
    private var etAccumulateDepreciation: EditText? = null
    private var etForwardedBudget: EditText? = null
    private var etWarranty: EditText? = null
    private var btnShowMore: Button? = null
    private var calendar: Calendar? = null
    private var date: DatePickerDialog.OnDateSetListener? = null
    private var selected: String? = null
    private var lastKey: String? = null
    private var itemId: String? = null
    private var serial: String? = null
    private val serialState: String? = null
    private var abbreviation: String? = null
    private var type: String? = null
    private var unnamed2: String? = null
    private var YY: String? = null
    private var path: Int = 0
    private var category: Int = 0
    private var branch: Int = 0
    private var order: Int = 0
    private var countDevice = 1
    private var quntity = 1
    private var updatedKey = 0
    private var progressBar: ProgressBar? = null
    private var progressDialogBackground: View? = null
    private var databaseReference: DatabaseReference? = null
    private var tvItemId: TextView? = null
    private val tvQuantity: TextView? = null
    private val tvClickToShow: TextView? = null
    private var spinnerAdapter: ArrayAdapter<CharSequence>? = null
    private var loadData: LoadData? = null
    private var moreData: LinearLayout? = null
    private var clickMore: Boolean? = false
    private var itemSave: ItemEntity? = null
    private var dateFormat: SimpleDateFormat? = null
    private var dateCheck: Date? = null
    private val clickListener = View.OnClickListener { view ->
        hideKeyboardFrom(context!!, getView()!!)
        if (view === btnShowMore) {
            if ((!clickMore)!!) {
                clickMore = true
                moreData!!.visibility = View.VISIBLE
            } else {
                clickMore = false
                moreData!!.visibility = View.INVISIBLE
            }
        }
    }
    private val onClickDate = View.OnClickListener {
        hideKeyboardFrom(context!!, view!!)
        if (itemId != null) {
            val d = etDatePicker!!.text.toString().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            DatePickerDialog(context!!,
                    date, Integer.parseInt(d[0]),
                    Integer.parseInt(d[1]) - 1,
                    Integer.parseInt(d[2])).show()
        } else {
            DatePickerDialog(context!!,
                    date, calendar!!.get(Calendar.YEAR),
                    calendar!!.get(Calendar.MONTH),
                    calendar!!.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private val onDateSet = DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
        calendar!!.set(Calendar.YEAR, year)
        calendar!!.set(Calendar.MONTH, monthOfYear)
        calendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }
    private val onTouchScan = View.OnTouchListener { view, motionEvent ->
        val DRAWABLE_RIGHT = 2

        if (motionEvent.action == MotionEvent.ACTION_UP) {
            if (motionEvent.x >= etSerialNumber!!.width - etSerialNumber!!.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                val intent = Intent(activity, ScanBarCodeAddDeviceActivity::class.java)
                startActivityForResult(intent, 12345)
                return@OnTouchListener true
            }
        }
        false
    }
    private val onSpinnerSelect = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
            if (adapterView === spType && itemId == null) {
                selected = adapterView.getItemAtPosition(i).toString()
                when (i) {
                    0 -> {
                        setSpinner(R.array.building, spTypeList!!)
                        category = 1
                    }
                    1 -> {
                        setSpinner(R.array.device_and_accessory, spTypeList!!)
                        category = 2
                    }
                    2 -> {
                        setSpinner(R.array.furniture, spTypeList!!)
                        category = 3
                    }
                    3 -> {
                        setSpinner(R.array.other, spTypeList!!)
                        category = 4
                    }
                }
            } else if (adapterView === spTypeList) {
                selected = adapterView.getItemAtPosition(i).toString()
                abbreviation = selected!!.toUpperCase().substring(0, 3)
                type = selected!!.toUpperCase()
            } else if (adapterView === spBranch) {
                when (i) {
                    0 -> branch = 1
                    1 -> branch = 2
                    2 -> branch = 3
                }
            }
        }

        override fun onNothingSelected(adapterView: AdapterView<*>) {
            selected = "none"
        }
    }
    private val onFocusChangeListener = View.OnFocusChangeListener { view, b ->
        if (view === etDevicePrice) {
            if (b) {
                etDevicePrice!!.hint = "1000.00"
            } else {
                etDevicePrice!!.hint = ""
            }
        } else if (view === etPurchasePrice) {
            if (b) {
                etPurchasePrice!!.hint = "1000.00"
            } else {
                etPurchasePrice!!.hint = ""
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_edit_detail, container, false)
        initInstances(view, savedInstanceState)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_device, menu)
        val menuItem = menu.findItem(R.id.action_check)
        MenuItemCompat.expandActionView(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_check) {
            hideKeyboardFrom(context!!, view!!)
            getUpdateKey()
            showAlertDialog("save")
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12345) {
            if (resultCode == Activity.RESULT_OK) {
                serial = data!!.getStringExtra("serial")
                checkSerial()
                etSerialNumber!!.setText(serial)
            }
        }
    }

    private fun initInstances(view: View, savedInstanceState: Bundle?) {
        loadData = LoadData(context!!)

        dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        dateCheck = Date()

        spBranch = view.findViewById(R.id.spinnerBranch)
        spType = view.findViewById(R.id.spinnerDeviceType)
        spTypeList = view.findViewById(R.id.spinnerDeviceTypeList)

        sp = context!!.getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE)
        editor = sp.edit()

        setSpinner(R.array.branch, spBranch!!)
        setSpinner(R.array.device_types, spType!!)

        tvItemId = view.findViewById(R.id.tvItemId)
        etOwnerName = view.findViewById(R.id.etOwnerName)
        etSerialNumber = view.findViewById(R.id.etSerialNumber)
        etDeviceDetail = view.findViewById(R.id.etDeviceDetail)
        etDatePicker = view.findViewById(R.id.etPurchaseDate)
        etOwnerId = view.findViewById(R.id.etOwnerId)
        etBrand = view.findViewById(R.id.etDeviceBrand)
        etDeviceModel = view.findViewById(R.id.etDeviceModel)
        etDevicePrice = view.findViewById(R.id.etPrice)
        etNote = view.findViewById(R.id.etNote)
        etPurchasePrice = view.findViewById(R.id.etDevicePurchasePrice)
        etQuantity = view.findViewById(R.id.etQuantity)
        btnShowMore = view.findViewById(R.id.btnShowMore)
        moreData = view.findViewById(R.id.hidedLayout)
        etForwardDepreciation = view.findViewById(R.id.etForwardDepreciation)
        etDepreciationRate = view.findViewById(R.id.etDepreciationRate)
        etDepreciationinYear = view.findViewById(R.id.etDepreciationinYear)
        etAccumulateDepreciation = view.findViewById(R.id.etAccumulateDepreciation)
        etForwardedBudget = view.findViewById(R.id.etForwardedBudget)
        etWarranty = view.findViewById(R.id.etWarranty)

        btnShowMore!!.setOnClickListener(clickListener)
        etPurchasePrice!!.onFocusChangeListener = onFocusChangeListener
        etDevicePrice!!.onFocusChangeListener = onFocusChangeListener

        calendar = Calendar.getInstance(TimeZone.getDefault())
        this.date = onDateSet
        etDatePicker!!.setOnClickListener(onClickDate)

        progressBar = view.findViewById(R.id.spin_kit)
        progressDialogBackground = view.findViewById(R.id.view)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Data")

        etSerialNumber!!.setOnTouchListener(onTouchScan)

        itemId = if (arguments != null) arguments!!.getString("Serial") else null
        if (itemId != null) {
            if (itemId!!.length < 14) {
                Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
            } else {
                //tvQuantity.setText(getResources().getString(R.string.quantity) + ":1");
                etQuantity!!.visibility = View.INVISIBLE
                lastKey = "" + loadData!!.selectData(itemId!!)!![0].autoId
                setData()
            }
        }
    }

    private fun setData() {
        tvItemId!!.text = itemId
        setSpinnerPosition(R.array.branch, spBranch, Integer.parseInt(itemId!!.substring(5, 6)), null)
        setSpinnerPosition(R.array.device_types, spType, Integer.parseInt(itemId!!.substring(6, 7)), null)
        val spinnerName: String
        spinnerName = itemId!!.substring(8, 11)
        when (Integer.parseInt(itemId!!.substring(6, 7))) {
            1 -> setSpinnerPosition(R.array.building, spTypeList, -1, spinnerName)
            2 -> setSpinnerPosition(R.array.device_and_accessory, spTypeList, -1, spinnerName)
            3 -> setSpinnerPosition(R.array.furniture, spTypeList, -1, spinnerName)
            4 -> setSpinnerPosition(R.array.other, spTypeList, -1, spinnerName)
        }
        itemEntity = loadData!!.selectData(itemId!!)
        if (itemEntity!!.size == 0) {
            return
        }
        etOwnerId!!.setText(itemEntity!![0].placeId)
        etOwnerName!!.setText(itemEntity!![0].placeName)
        etBrand!!.setText(itemEntity!![0].brand)
        etSerialNumber!!.setText(itemEntity!![0].serialNo)
        etDeviceDetail!!.setText(itemEntity!![0].detail)
        etDeviceModel!!.setText(itemEntity!![0].model)
        etDevicePrice!!.setText(itemEntity!![0].price)
        etPurchasePrice!!.setText(itemEntity!![0].purchasedPrice)
        etDatePicker!!.setText(setDateFromRoom(itemEntity!![0].purchasedDate!!))
        etNote!!.setText(itemEntity!![0].note)
        etForwardDepreciation!!.setText(itemEntity!![0].forwardDepreciation)
        etDepreciationRate!!.setText(itemEntity!![0].depreciationRate)
        etDepreciationinYear!!.setText(itemEntity!![0].depreciationYear)
        etAccumulateDepreciation!!.setText(itemEntity!![0].accumulatedDepreciation)
        etForwardedBudget!!.setText(itemEntity!![0].forwardedBudget)
        etWarranty!!.setText(itemEntity!![0].warrantyDate)
    }

    private fun setSpinner(spinnerlist: Int, spinner: Spinner) {
        spinnerAdapter = ArrayAdapter.createFromResource(
                context!!,
                spinnerlist,
                R.layout.spinner_item)
        spinnerAdapter!!.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = onSpinnerSelect
    }

    private fun setSpinnerPosition(spinerlist: Int, spinner: Spinner?, position: Int, spinerName: String?) {
        if (position == -1) {
            spinnerAdapter = ArrayAdapter.createFromResource(
                    context!!,
                    spinerlist,
                    R.layout.spinner_item)
            spinnerAdapter!!.setDropDownViewResource(R.layout.spinner_item)
            spinner!!.adapter = spinnerAdapter
            val array = resources.getStringArray(spinerlist)
            val list = Arrays.asList(*array)
            var spinnerPosition = 0

            for (str in list) {
                if (str.contains(spinerName!!)) {
                    spinnerPosition = list.indexOf(str)
                }
            }
            spinner.setSelection(spinnerPosition, true)
        } else {
            spinner!!.setSelection(position - 1)
        }
    }

    private fun showAlertDialog(type: String) {
        val builder = AlertDialog.Builder(activity!!)
        var dialogMsg = 0
        when (type) {
            "save" -> dialogMsg = R.string.dialog_msg_confirm
            "serial" -> {
                dialogMsg = R.string.dialog_msg_check_serial
                builder.setTitle(R.string.dialog_msg_head_check_serial)
            }
        }

        if (itemId == null) {
            lastKey = getLastKey()
        }

        builder.setMessage(dialogMsg).setPositiveButton("Yes") { dialog, which ->
            if (type.matches("save".toRegex()) && checkForm()) {
                YY = etDatePicker!!.text.toString().substring(8, 10)

                if (tvItemId!!.text.toString().matches("Item Id".toRegex())) {
                    order = 1
                    val form = "DGO$YY$branch$category"

                    val itemEntity = loadData!!.item
                    for (i in itemEntity.indices) {
                        if (itemEntity[i].unnamed2!!.contains(form))
                            order++
                    }
                    var count = 0
                    try {
                        count = Integer.parseInt(etQuantity!!.text.toString())
                        quntity = count
                    } catch (num: NumberFormatException) {
                        Toast.makeText(context, "" + num, Toast.LENGTH_SHORT).show()
                    }

                    for (i in 0 until count) {
                        setUpdatedId(lastKey)
                        saveData()
                        val key = Integer.parseInt(lastKey) + 1
                        lastKey = key.toString() + ""
                        order++
                    }

                } else {
                    progressDialogBackground!!.visibility = View.VISIBLE
                    progressBar!!.visibility = View.VISIBLE

                    unnamed2 = tvItemId!!.text.toString()
                    updateData()
                }

            } else if (type.matches("serial".toRegex())) {
                Toast.makeText(activity, "Intent to Detail", Toast.LENGTH_SHORT).show()
                /*Intent intent = new Intent(getContext(), DeviceDetailActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
            }
        }.setNegativeButton("No") { dialog, which -> Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show() }
        val dialog = builder.create()
        dialog.show()

        dialog.window!!.setBackgroundDrawableResource(android.R.color.white)
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(R.color.white))
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun updateData() {
        val autoId = itemEntity!![0].autoId
        val date = etDatePicker!!.text.toString()
        val order = tvItemId!!.text.toString().substring(10)
        val item = DataItem("ID", etOwnerId!!.text.toString(), etOwnerName!!.text.toString(),
                etBrand!!.text.toString(), etSerialNumber!!.text.toString(), etDeviceModel!!.text.toString(),
                etDeviceDetail!!.text.toString(), etDevicePrice!!.text.toString(), etPurchasePrice!!.text.toString(),
                setDate(date), etNote!!.text.toString(), type!!, getUnnamed2(), etForwardDepreciation!!.text.toString(),
                etDepreciationRate!!.text.toString(), etDepreciationinYear!!.text.toString(),
                etAccumulateDepreciation!!.text.toString(), etForwardedBudget!!.text.toString(), "" + YY!!,
                getUnnamed2().substring(3), "" + category, "" + branch, "-",
                "-", "" + dateFormat!!.format(dateCheck!!), "" + order,
                "" + abbreviation!!, "-", "DGO", etWarranty!!.text.toString())

        databaseReference!!.child(lastKey!!).setValue(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                setUpdatedId(lastKey)
                loadData!!.updateItem(dateFormat!!.format(dateCheck!!), etOwnerName!!.text.toString(), etOwnerId!!.text.toString(),
                        etBrand!!.text.toString(), etSerialNumber!!.text.toString(), etDeviceDetail!!.text.toString(),
                        etDeviceModel!!.text.toString(), etWarranty!!.text.toString(), etPurchasePrice!!.text.toString(),
                        saveDateToDB(etDatePicker!!.text.toString()), etDevicePrice!!.text.toString(), etNote!!.text.toString(),
                        etForwardDepreciation!!.text.toString(), etDepreciationRate!!.text.toString(),
                        etDepreciationinYear!!.text.toString(), etAccumulateDepreciation!!.text.toString(),
                        etForwardedBudget!!.text.toString(), autoId)
                val intentBack = Intent()
                intentBack.putExtra("itemId", itemEntity!![0].unnamed2)
                activity!!.setResult(Activity.RESULT_OK, intentBack)
                activity!!.finish()
            } else {
                Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                progressDialogBackground!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.INVISIBLE
            }
        }
    }

    private fun saveData() {
        progressDialogBackground!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
        val date = setDate(etDatePicker!!.text.toString())
        // TODO: Add more item data
        //TODO:รหัสทรัพสิน ID
        val item = DataItem("ID", etOwnerId!!.text.toString(), etOwnerName!!.text.toString(),
                etBrand!!.text.toString(), etSerialNumber!!.text.toString(), etDeviceModel!!.text.toString(),
                etDeviceDetail!!.text.toString(), etDevicePrice!!.text.toString(), etPurchasePrice!!.text.toString(),
                setDate(date), etNote!!.text.toString(), type!!, getUnnamed2(), etForwardDepreciation!!.text.toString(),
                etDepreciationRate!!.text.toString(), etDepreciationinYear!!.text.toString(),
                etAccumulateDepreciation!!.text.toString(), etForwardedBudget!!.text.toString(), "" + YY!!,
                getUnnamed2().substring(3), "" + category, "" + branch, "-",
                "-", "" + dateFormat!!.format(dateCheck!!), "" + order,
                "" + abbreviation!!, "-", "DGO", etWarranty!!.text.toString())

        itemSave = ItemEntity(loadData!!.item.size, getUnnamed2(), type!!, etDeviceDetail!!.text.toString(),
                etSerialNumber!!.text.toString(), etOwnerName!!.text.toString(), saveDateToDB(date), etNote!!.text.toString(),
                "-", etOwnerId!!.text.toString(), getUnnamed2().substring(3), etDevicePrice!!.text.toString(),
                etDeviceModel!!.text.toString(), etDepreciationRate!!.text.toString(), "ID", etBrand!!.text.toString(),
                abbreviation!!, order.toString() + "", "-", YY!! + "", "DGO", "-",
                etForwardedBudget!!.text.toString(), etAccumulateDepreciation!!.text.toString(),
                etWarranty!!.text.toString(), etDepreciationinYear!!.text.toString(), branch.toString() + "",
                "" + category, etPurchasePrice!!.text.toString(), etForwardedBudget!!.text.toString(),
                etForwardDepreciation!!.text.toString(), dateFormat!!.format(dateCheck!!))
        if (lastKey != null) {
            databaseReference!!.child(lastKey!!).setValue(item).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    if (countDevice == quntity) {
                        loadData!!.insert(itemSave!!)
                        Toast.makeText(activity, "Complete!", Toast.LENGTH_SHORT).show()
                        progressDialogBackground!!.visibility = View.INVISIBLE
                        progressBar!!.visibility = View.INVISIBLE
                        val intent = Intent()
                        intent.putExtra("itemId", itemSave!!.unnamed2)
                        activity!!.finish()
                    }
                    countDevice++
                } else {
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    progressDialogBackground!!.visibility = View.INVISIBLE
                    progressBar!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun getUpdateKey() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Updated")
        val query = databaseReference.orderByKey().limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (s in dataSnapshot.children) {
                    updatedKey = Integer.parseInt(s.key) + 1
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                updatedKey = -1
            }
        })
    }

    private fun setUpdatedId(lastKey: String?) {
        FirebaseDatabase.getInstance().reference.child("Updated")
                .child(updatedKey.toString() + "").child("id").setValue(lastKey)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Complete!", Toast.LENGTH_SHORT).show()

                        // TODO: Add Success SuccessDialog
                        progressDialogBackground!!.visibility = View.INVISIBLE
                        progressBar!!.visibility = View.INVISIBLE
                    }
                }
        updatedKey++
    }

    private fun setDate(inputDate: String): String {
        var inputDate = inputDate
        if (inputDate.contains("GMT")) {
            inputDate = inputDate.substring(0, inputDate.indexOf("GMT")).trim { it <= ' ' }
        }
        val inputFormat = "dd/MM/yyyy"
        val inputDateFormat = SimpleDateFormat(
                inputFormat, Locale.ENGLISH)
        val outputFormat = "EEE MMM dd yyyy HH:mm:ss"
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

    private fun setDateFromRoom(inputDate: String): String {
        var inputDate = inputDate
        if (inputDate.contains("GMT")) {
            inputDate = inputDate.substring(0, inputDate.indexOf("GMT")).trim { it <= ' ' }
        }
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

    private fun saveDateToDB(inputDate: String): String {
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

    private fun getUnnamed2(): String {
        if (unnamed2 == null) {
            val date = etDatePicker!!.text.toString()
            YY = date.substring(8, 10)
            return "DGO" + YY + branch + category + "-" + abbreviation + getOrder(order)
        }
        return unnamed2
    }

    private fun getOrder(order: Int): String {
        var order = order
        val num: String
        if (order < 1) {
            num = "001"
            order++
        } else if (order < 10) {
            num = "00$order"
        } else if (order < 100) {
            num = "0$order"
        } else {
            num = "" + order
        }
        return num
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        etDatePicker!!.setText(sdf.format(calendar!!.time))
    }

    private fun getLastKey(): String? {
        val query = databaseReference!!.orderByKey().limitToLast(1)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (s in dataSnapshot.children) {
                    lastKey = s.key
                    path = Integer.parseInt(lastKey) + 1
                    lastKey = path.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Cannot Insert Data", Toast.LENGTH_SHORT).show()
                progressDialogBackground!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.INVISIBLE
                lastKey = ""
            }
        })
        return lastKey
    }

    private fun checkSerial() {
        val itemEntities = loadData!!.item
        for (i in itemEntities.indices) {
            if (itemEntities[i].serialNo!!.matches(serial.toRegex())) {
                showAlertDialog("serial")
            }
        }
    }

    private fun checkForm(): Boolean {

        if (type == null || type!!.length == 0) {
            type = spTypeList!!.selectedItem.toString()
        }

        if (YY == null || YY!!.length == 0) {
            YY = etDatePicker!!.text.toString().substring(8, 10)
        }

        if (branch == 0) {
            branch = spBranch!!.selectedItemPosition + 1
        }

        if (category == 0) {
            category = spType!!.selectedItemPosition + 1
        }

        if (abbreviation == null || abbreviation!!.length == 0) {
            abbreviation = spTypeList!!.selectedItem.toString()
            if (abbreviation!!.length > 3) {
                abbreviation = abbreviation!!.substring(0, 3)
            }
        }

        if (TextUtils.isEmpty(etDeviceDetail!!.text)) {
            etDeviceDetail!!.setText("-")
        }
        if (TextUtils.isEmpty(etOwnerName!!.text)) {
            etOwnerName!!.setText("-")
        }
        if (TextUtils.isEmpty(etDeviceDetail!!.text)) {
            etDeviceDetail!!.setText("-")
        }
        if (TextUtils.isEmpty(etSerialNumber!!.text)) {
            etSerialNumber!!.setText("-")
        }
        if (TextUtils.isEmpty(etBrand!!.text)) {
            etBrand!!.setText("-")
        }
        if (TextUtils.isEmpty(etNote!!.text)) {
            etNote!!.setText("-")
        }
        if (TextUtils.isEmpty(etOwnerId!!.text)) {
            etOwnerId!!.setText("-")
        }
        if (TextUtils.isEmpty(etDeviceModel!!.text)) {
            etDeviceModel!!.setText("-")
        }
        if (TextUtils.isEmpty(etForwardDepreciation!!.text)) {
            etForwardDepreciation!!.setText("-")
        }
        if (TextUtils.isEmpty(etDepreciationRate!!.text)) {
            etDepreciationRate!!.setText("-")
        }
        if (TextUtils.isEmpty(etDepreciationinYear!!.text)) {
            etDepreciationinYear!!.setText("-")
        }
        if (TextUtils.isEmpty(etAccumulateDepreciation!!.text)) {
            etAccumulateDepreciation!!.setText("-")
        }
        if (TextUtils.isEmpty(etForwardedBudget!!.text)) {
            etForwardedBudget!!.setText("-")
        }
        if (TextUtils.isEmpty(etWarranty!!.text)) {
            etWarranty!!.setText("-")
        }

        if (TextUtils.isEmpty(etDevicePrice!!.text) && TextUtils.isEmpty(etPurchasePrice!!.text)) {
            Toast.makeText(context, "Please input price", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(etDevicePrice!!.text)) {
            etDevicePrice!!.setText("-")
        }
        if (TextUtils.isEmpty(etPurchasePrice!!.text)) {
            etPurchasePrice!!.setText("-")
        }
        if (TextUtils.isEmpty(etDatePicker!!.text)) {
            Toast.makeText(context, "Please input purchased date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(etQuantity!!.text)) {
            etDeviceModel!!.setText("1")
            Toast.makeText(context, "1 piece", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    companion object {

        fun newInstances(): AddDeviceFragment {
            val fragment = AddDeviceFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        private fun hideKeyboardFrom(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
