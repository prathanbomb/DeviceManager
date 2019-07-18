package com.example.devicemanager.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.devicemanager.R
import com.example.devicemanager.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.contentContainer, LoginFragment.newInstance())
                    .commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
