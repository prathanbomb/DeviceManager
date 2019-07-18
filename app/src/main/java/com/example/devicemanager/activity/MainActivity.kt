package com.example.devicemanager.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.devicemanager.R
import com.example.devicemanager.fragment.LoginFragment
import com.example.devicemanager.fragment.MainFragment
import com.example.devicemanager.fragment.SummaryFragment
import com.example.devicemanager.manager.LoadData
import com.example.devicemanager.room.ItemEntity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private var loadData: LoadData? = null
    private var sp: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var view: View? = null
    private var progressBar: ProgressBar? = null
    private var btnSummary: Button? = null
    private var btnDetail: Button? = null
    private var insertStatus: Int = 0
    private val onBtnClick = View.OnClickListener { view ->
        val mainFragment = supportFragmentManager.findFragmentByTag("MainFragment") as MainFragment?
        val secondFragment = supportFragmentManager.findFragmentByTag("SummaryFragment") as SummaryFragment?

        if (view === btnDetail) {
            if (mainFragment == null && secondFragment == null) {
                setStartFragment()
            } else if (mainFragment == null) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.contentContainer, MainFragment.newInstance(), "MainFragment")
                        .detach(secondFragment!!)
                        .commit()
            } else if (secondFragment != null) {
                supportFragmentManager.beginTransaction()
                        .attach(mainFragment)
                        .detach(secondFragment)
                        .commit()
            }
            btnDetail!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            btnSummary!!.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        } else if (view === btnSummary) {
            if (mainFragment == null && secondFragment == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.contentContainer, SummaryFragment.newInstance(), "SummaryFragment")
                        .commit()
            } else if (secondFragment == null) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.contentContainer, SummaryFragment.newInstance(), "SummaryFragment")
                        .detach(mainFragment!!)
                        .commit()
            } else if (mainFragment != null) {
                supportFragmentManager.beginTransaction()
                        .attach(secondFragment)
                        .detach(mainFragment)
                        .commit()
            }
            btnSummary!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            btnDetail!!.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        initInstances()

        val logout = intent.getStringExtra("logout")

        if (logout != null && logout.matches("true".toRegex())) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, LoginFragment.newInstance())
                    .commit()
        } else {
            mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user == null) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showLoadingView()
                    setUpLoadData()
                    //setStartFragment();
                }
            }
            mAuth!!.addAuthStateListener(mAuthListener!!)
        }
    }

    private fun initInstances() {
        btnDetail = findViewById(R.id.btnDetail)
        btnSummary = findViewById(R.id.btnSummary)
        view = findViewById(R.id.view)
        progressBar = findViewById(R.id.spin_kit)

        btnDetail!!.setOnClickListener(onBtnClick)
        btnSummary!!.setOnClickListener(onBtnClick)

        sp = this.getSharedPreferences("DownloadStatus", Context.MODE_PRIVATE)

        loadData = LoadData(this)
    }

    override fun onResume() {
        super.onResume()
        if (intent.getStringExtra("itemId") != null) {
            SuccessDialog()
            setUpLoadData()
        }
    }

    public override fun onStop() {
        super.onStop()
        editor = sp!!.edit()
        editor!!.clear()
        editor!!.apply()

        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    private fun setUpLoadData() {
        Log.d("test1707", sp!!.getBoolean("downloadStatus", true).toString() + " Status")
        if (sp!!.getBoolean("downloadStatus", true)) {
            if (loadData!!.deleteTable() == 1) {
                loadData()
            }
        } else {
            hideLoadingView()
            setStartFragment()
        }
    }

    private fun loadData() {
        insertStatus = 0
        val databaseReference = FirebaseDatabase.getInstance().reference.child("Data")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                editor = sp!!.edit()
                editor!!.putBoolean("downloadStatus", false)
                editor!!.apply()

                for (s in dataSnapshot.children) {
                    val item = s.getValue(ItemEntity::class.java)

                    if (item != null) {
                        if (!item.purchasedDate!!.matches("".toRegex()) && !item.purchasedDate!!.matches("-".toRegex())) {
                            item.purchasedDate = setDate(item.purchasedDate!!)
                        }
                        item.autoId = Integer.parseInt(s.key!!)
                        insertStatus = loadData!!.insert(item)!!
                    }
                }

                if (insertStatus == 1) {
                    setStartFragment()
                    hideLoadingView()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Snackbar.make(view!!, "Download Failed", Snackbar.LENGTH_SHORT)
                        .setAction("Reload") { setUpLoadData() }.show()
            }
        })
    }

    private fun setStartFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, MainFragment.newInstance(), "MainFragment")
                .commit()
        btnDetail!!.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
    }

    private fun showLoadingView() {
        view!!.visibility = View.VISIBLE
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        view!!.visibility = View.INVISIBLE
        progressBar!!.visibility = View.INVISIBLE
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

    private fun SuccessDialog() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                .show()
    }
}
