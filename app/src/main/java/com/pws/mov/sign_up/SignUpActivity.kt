package com.pws.mov.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.pws.mov.R
import com.pws.mov.model.Users
import com.pws.mov.sign_in.SignInActivity
import com.pws.mov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_password
import kotlinx.android.synthetic.main.activity_sign_up.et_username
import java.time.Instant

class SignUpActivity : AppCompatActivity() {

    lateinit var vuserName: String
    lateinit var vpassword: String
    lateinit var vnama: String
    lateinit var vemail: String

    //firebase
    private lateinit var mFirebaseReference: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()
        mFirebaseReference = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        btn_back.setOnClickListener {
            val back = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(back)
            finishAffinity()
        }


        btn_continue.setOnClickListener {
            vuserName = et_username.text.toString()
            vpassword = et_password.text.toString()
            vnama = et_nama.text.toString()
            vemail = et_email.text.toString()

            if (vuserName.equals("")) {
                et_username.error = "Please Input Username"
                et_username.requestFocus()
            } else if (vpassword.equals("")) {
                et_password.error = "Please Input Password"
                et_password.requestFocus()
            } else if (vnama.equals("")) {
                et_nama.error = "Please Input Name"
                et_nama.requestFocus()
            } else if (vemail.equals("")) {
                et_email.error = "Please Input E-Mail"
                et_email.requestFocus()
            } else {
                saveAccount(vuserName, vpassword, vnama, vemail)
            }
        }
    }

    private fun saveAccount(vuserName: String, vpassword: String, vnama: String, vemail: String) {
        val users = Users()
        users.username = vuserName
        users.password = vpassword
        users.nama = vnama
        users.email = vemail

        if (vuserName != null) {
            checkUsername(vuserName, users)
        }
    }

    private fun checkUsername(vuserName: String, data: Users) {
        mFirebaseReference.child(vuserName).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var users = dataSnapshot.getValue(Users::class.java)
                if (users != null) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Username Already Exists",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    mFirebaseReference.child(vuserName).setValue(data)
                    preferences.setValue("nama", data.nama.toString())
                    preferences.setValue("user", data.username.toString())
                    preferences.setValue("url", "")
                    preferences.setValue("email", data.email.toString())

                    var moveSignUpPhotoScreen =
                        Intent(this@SignUpActivity, SignUpPhotoScreenActivity::class.java).putExtra(
                            "nama",
                            data.nama
                        )
                    startActivity(moveSignUpPhotoScreen)
                }
            }
        })
    }
}