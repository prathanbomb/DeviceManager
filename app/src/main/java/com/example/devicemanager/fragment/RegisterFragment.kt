package com.example.devicemanager.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterFragment : Fragment() {

    internal var progressBar: ProgressBar
    internal var progressDialogBackground: View
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etCode: EditText? = null
    private var strEmail: String? = null
    private var strPassword: String? = null
    private var strCode: String? = null
    private var code: String? = null
    private val state: Boolean = false
    private var mAuth: FirebaseAuth? = null
    private var btnSubmit: Button? = null
    private var tvLogin: TextView? = null
    private val mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var context: Context? = null
    private val onClickSubmit = View.OnClickListener { view ->
        hideKeyboardFrom(context!!, view)
        progressDialogBackground.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        if (checkForm()) {
            registerUser()
        }
        //            progressDialogBackground.setVisibility(View.INVISIBLE);
        //            progressBar.setVisibility(View.INVISIBLE);
    }
    private val onClickLogin = View.OnClickListener {
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.contentContainer, LoginFragment.newInstance())
                .commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        initInstances(view)
        return view
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener)
        }
    }

    private fun initInstances(view: View) {

        context = Contextor.getInstance().context

        etEmail = view.findViewById(R.id.etRegEmail)
        etPassword = view.findViewById(R.id.etRegPassword)
        etCode = view.findViewById(R.id.etRegInviteCode)
        btnSubmit = view.findViewById(R.id.btnRegSubmit)
        tvLogin = view.findViewById(R.id.tvRegLogin)

        progressBar = view.findViewById(R.id.spin_kit)
        progressDialogBackground = view.findViewById(R.id.view)

        mAuth = FirebaseAuth.getInstance()

        code = getCode()

        btnSubmit!!.setOnClickListener(onClickSubmit)
        tvLogin!!.setOnClickListener(onClickLogin)
    }

    private fun registerUser() {
        strEmail = etEmail!!.text.toString().trim { it <= ' ' }
        strPassword = etPassword!!.text.toString().trim { it <= ' ' }

        mAuth!!.createUserWithEmailAndPassword(strEmail!!, strPassword!!)
                .addOnFailureListener { e ->
                    progressDialogBackground.visibility = View.INVISIBLE
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    Log.d("isSuccessful", e.message)
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Register Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
    }

    private fun getCode(): String? {
        val databaseReference = FirebaseDatabase.getInstance()
                .reference.child("InvitationCode")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (s in dataSnapshot.children) {
                    code = s.child("Code").getValue(String::class.java)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Cannot connect to Firebase", Toast.LENGTH_SHORT).show()
                Log.d("Firebase", databaseError.message)
                code = ""
            }
        })
        return code
    }

    private fun checkForm(): Boolean {
        strCode = etCode!!.text.toString().trim { it <= ' ' }
        strEmail = etEmail!!.text.toString().trim { it <= ' ' }
        strPassword = etPassword!!.text.toString().trim { it <= ' ' }
        strCode = etCode!!.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword) || TextUtils.isEmpty(strCode)) {
            closeLoadingDialog()
            Toast.makeText(context, "Please insert all the fields", Toast.LENGTH_SHORT).show()
            return false
        } else if (isAdded && !strEmail!!.contains(resources.getString(R.string.digio_email))) {
            closeLoadingDialog()
            Toast.makeText(context, "Wrong E-Mail address", Toast.LENGTH_SHORT).show()
            return false
        } else if (strPassword!!.length < 6) {
            closeLoadingDialog()
            Toast.makeText(context, "Password must contain at least 6 letters", Toast.LENGTH_SHORT).show()
            return false
        } else if (code!!.matches("".toRegex())) {
            closeLoadingDialog()
            Toast.makeText(context, "Connection error, try again later.", Toast.LENGTH_SHORT).show()
            return false
        } else if (!strCode!!.matches(code.toRegex())) {
            closeLoadingDialog()
            Toast.makeText(context, "Incorrect Invitation Code", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun closeLoadingDialog() {
        progressDialogBackground.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    companion object {

        fun newInstance(): RegisterFragment {
            val fragment = RegisterFragment()
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
