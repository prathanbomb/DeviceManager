package com.example.devicemanager.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.devicemanager.R
import com.example.devicemanager.activity.AddDeviceActivity
import com.example.devicemanager.manager.LoadData
import com.example.devicemanager.room.ItemEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DeviceDetailFragment : Fragment() {
    internal var itemEntity: List<ItemEntity>? = null
    private var tvSerialNumber: TextView? = null
    private var tvOwnerName: TextView? = null
    private var tvDeviceDetail: TextView? = null
    private var tvLastUpdate: TextView? = null
    private var tvAddedDate: TextView? = null
    private var tvType: TextView? = null
    private var tvItemId: TextView? = null
    private var tvBrand: TextView? = null
    private var tvModel: TextView? = null
    private var btnCheck: Button? = null
    private var btnEdit: Button? = null
    private var progressBar: ProgressBar? = null
    private var progressDialogBackground: View? = null
    private var loadData: LoadData? = null
    private var updatedKey: Int = 0
    private var lastKey: String? = null
    private val clickListener = View.OnClickListener { view ->
        if (view === btnEdit) {
            val intent = Intent(activity, AddDeviceActivity::class.java)
            intent.putExtra("Serial", serial)
            startActivityForResult(intent, 11111)
        } else if (view === btnCheck) {
            showAlertDialog(R.string.dialog_msg_checked, "check")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_device, container, false)
        initInstances(view)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11111) {
            if (resultCode == Activity.RESULT_OK) {
                loadData = LoadData(activity!!)
                getData(serial)
                SuccessDialog()
            }
        }
    }

    private fun initInstances(view: View) {

        loadData = LoadData(context!!)

        tvSerialNumber = view.findViewById(R.id.tvSerialNumber)
        tvOwnerName = view.findViewById(R.id.tvOwnerName)
        tvDeviceDetail = view.findViewById(R.id.tvDeviceDetail)
        tvAddedDate = view.findViewById(R.id.tvAddedDate)
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate)
        tvType = view.findViewById(R.id.tvType)
        tvItemId = view.findViewById(R.id.tvItemId)
        tvBrand = view.findViewById(R.id.tvBrand)
        tvModel = view.findViewById(R.id.tvModel)

        btnEdit = view.findViewById(R.id.btnEdit)
        btnCheck = view.findViewById(R.id.btnCheck)
        btnEdit!!.setOnClickListener(clickListener)
        btnCheck!!.setOnClickListener(clickListener)

        progressBar = view.findViewById(R.id.spin_kit)
        progressDialogBackground = view.findViewById(R.id.view)

        getUpdateKey()

        if (serial != null) {
            getData(serial)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getData(serialNew: String?) {
        progressDialogBackground!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE

        itemEntity = loadData!!.selectData(serialNew!!)

        if (itemEntity != null) {
            lastKey = itemEntity!![0].autoId.toString() + ""

            tvItemId!!.text = "Item ID : " + itemEntity!![0].unnamed2!!
            tvOwnerName!!.text = checkNoneData(itemEntity!![0].placeName!!, "No Owner")
            tvDeviceDetail!!.text = checkNoneData(itemEntity!![0].detail!!, "N/A")
            tvBrand!!.text = checkNoneData(itemEntity!![0].brand!!, "N/A")
            tvType!!.text = itemEntity!![0].type
            tvModel!!.text = "Model : " + checkNoneData(itemEntity!![0].model!!, "N/A")
            tvSerialNumber!!.text = "S/N : " + checkNoneData(itemEntity!![0].serialNo!!, "No Serial")

            tvLastUpdate!!.text = resources.getString(R.string.last_check) + " : " + itemEntity!![0].lastUpdated
            tvAddedDate!!.text = resources.getString(R.string.added_date) + " : " + setDate(itemEntity!![0].purchasedDate)
            hideDialog()
        } else {
            activity!!.finish()
            hideDialog()
        }
    }

    private fun checkNoneData(data: String, text: String): String {

        return if (data.trim { it <= ' ' }.matches("-".toRegex())) {
            text
        } else {
            data
        }

    }

    private fun hideDialog() {
        progressDialogBackground!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.INVISIBLE
    }

    private fun showAlertDialog(msg: Int, state: String) {
        val builder = AlertDialog.Builder(activity!!)
        val dialogMsg = resources.getString(msg)

        builder.setMessage(dialogMsg).setPositiveButton("Yes") { dialog, which ->
            if (state.matches("check".toRegex())) {
                checkedDevice()
                setUpdatedId(lastKey)
                SuccessDialog()
            } else if (state.matches("add".toRegex())) {
                val intent = Intent(activity, AddDeviceActivity::class.java)
                intent.putExtra("Serial", serial)
                startActivity(intent)
                activity!!.finish()
            }
        }.setNegativeButton("No") { dialog, which ->
            Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
            if (state.matches("add".toRegex())) {
                activity!!.finish()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun checkedDevice() {
        progressDialogBackground!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
        val idKey = loadData!!.selectData(serial!!)!![0].autoId.toString() + ""
        val databaseReference = FirebaseDatabase.getInstance().reference
                .child("Data").child(idKey).child("lastUpdated")
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date = Date()
        databaseReference.setValue(dateFormat.format(date))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val autoId = itemEntity!![0].autoId
                        hideDialog()
                        loadData!!.updateLastUpdate(dateFormat.format(date), autoId)
                        tvLastUpdate!!.text = "Last Check : " + loadData!!.item[autoId].lastUpdated!!
                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e -> Log.d("checkedDevice", e.toString()) }
    }

    private fun getUpdateKey() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Updated")
        val query = databaseReference.orderByKey().limitToLast(1)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (s in dataSnapshot.children) {
                    updatedKey = Integer.parseInt(s.key!!) + 1
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
                        updatedKey++
                    }
                }
    }

    private fun setDate(inputDate: String?): String {
        val inputFormat = "yyyy-MM-dd"
        val inputDateFormat = SimpleDateFormat(
                inputFormat, Locale.ENGLISH)
        val outputFormat = "dd/MM/yyyy"
        val outputDateFormat = SimpleDateFormat(
                outputFormat, Locale.ENGLISH)

        val date: Date?
        var str = inputDate ?: ""

        try {
            date = inputDateFormat.parse(inputDate!!)
            str = outputDateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str

    }

    private fun SuccessDialog() {
        SweetAlertDialog(activity!!, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                .show()
    }

    companion object {

        private var serial: String? = null

        fun newInstances(barcode: String): DeviceDetailFragment {
            val fragment = DeviceDetailFragment()
            serial = barcode
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
