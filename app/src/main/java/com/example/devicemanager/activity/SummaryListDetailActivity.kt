package com.example.devicemanager.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.example.devicemanager.R
import com.example.devicemanager.fragment.SummaryListDetailFragment

class SummaryListDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_list_detail)
        title = intent.getStringExtra("Type")
        if (savedInstanceState == null) {
            val bundle = Bundle()

            bundle.putString("Type", intent.getStringExtra("Type"))
            bundle.putString("Brand", intent.getStringExtra("Brand"))

            val fragment = SummaryListDetailFragment.newInstance()
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer, fragment)
                    .commit()
        }
        initInstances()
    }

    private fun initInstances() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
