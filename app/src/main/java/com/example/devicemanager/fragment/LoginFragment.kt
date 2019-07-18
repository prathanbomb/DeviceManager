package com.example.devicemanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.devicemanager.R
import com.example.devicemanager.activity.MainActivity
import com.example.devicemanager.manager.Contextor
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    internal var progressBar: ProgressBar
    internal var progressDialogBackground: View
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var strEmail: String? = null
    private var strPassword: String? = null
    private var btnSubmit: Button? = null
    private var tvRegister: TextView? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val onClickBtnSubmit = View.OnClickListener { view ->
        hideKeyboardFrom(context!!, view)
        progressDialogBackground.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        if (checkEmail()) {
            mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                if (user == null) {
                    userLogin()
                }
            }
            mAuth!!.addAuthStateListener(mAuthListener!!)
        }
    }
    private val onClickRegister = View.OnClickListener {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, RegisterFragment.newInstance())
                .commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        initInstances(view)
        return view
    }

    override fun onPause() {
        super.onPause()
        etEmail!!.text.clear()
        etPassword!!.text.clear()
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    private fun initInstances(view: View) {
        progressBar = view.findViewById(R.id.spin_kit)
        progressDialogBackground = view.findViewById(R.id.view)

        etEmail = view.findViewById(R.id.etLoginEmail)
        etPassword = view.findViewById(R.id.etLoginPassword)
        btnSubmit = view.findViewById(R.id.btnLoginSubmit)

        btnSubmit!!.setOnClickListener(onClickBtnSubmit)

        tvRegister = view.findViewById(R.id.tvRegister)
        tvRegister!!.setOnClickListener(onClickRegister)

        mAuth = FirebaseAuth.getInstance()
    }

    private fun userLogin() {
        strEmail = etEmail!!.text.toString().trim { it <= ' ' }
        strPassword = etPassword!!.text.toString().trim { it <= ' ' }

        if (checkForm(strEmail, strPassword)) {
            mAuth!!.signInWithEmailAndPassword(strEmail!!, strPassword!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(Contextor.getInstance().context,
                                    "Login Success", Toast.LENGTH_SHORT).show()
                            progressDialogBackground.visibility = View.INVISIBLE
                            progressBar.visibility = View.INVISIBLE
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity!!.finish()
                        } else {
                            Toast.makeText(Contextor.getInstance().context,
                                    "Failed to Login", Toast.LENGTH_SHORT).show()
                            progressDialogBackground.visibility = View.INVISIBLE
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
        }

    }

    private fun checkForm(strEmail: String, strPassword: String): Boolean {
        if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)) {
            Toast.makeText(activity, "Please insert all the fields", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun checkEmail(): Boolean {
        val email = etEmail!!.text.toString().toLowerCase()
        if (email.contains(resources.getString(R.string.digio_email))) {
            return true
        } else {
            progressDialogBackground.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(context, "Incorrect E-mail", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    companion object {

        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
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
