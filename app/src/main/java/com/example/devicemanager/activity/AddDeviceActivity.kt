package com.example.devicemanager.activity

import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.example.devicemanager.R
import com.example.devicemanager.fragment.AddDeviceFragment

class AddDeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

        initInstances()

        if (savedInstanceState == null) {

            val bundle = Bundle()

            bundle.putString("Path", intent.getStringExtra("Path"))
            bundle.putString("Serial", intent.getStringExtra("Serial"))
            val fragment = AddDeviceFragment.newInstances()
            fragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer, fragment)
                    .commit()
        }
    }

    private fun initInstances() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Add Device</font>")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
