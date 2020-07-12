package com.pws.mov.sign_in

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.pws.mov.HomeScreenActivity
import com.pws.mov.R
import com.pws.mov.model.Users
import com.pws.mov.sign_up.SignUpActivity
import com.pws.mov.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    lateinit var vuserName: String
    lateinit var vpassword: String

    //InisialisasiFirebase
    lateinit var mDatabase: DatabaseReference

    //PreferenceLogin
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //PathFromDatabaseName
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        //Pref
        preferences = Preferences(this)

        //PageOnBoardingDoNotShowagain(Only For first time)
        preferences.setValue("onboarding", "1")
        if (preferences.getValue("status").equals("1")) {
            finishAffinity()

            var moveHomeScreen = Intent(this@SignInActivity, HomeScreenActivity::class.java)
            startActivity(moveHomeScreen)
        }

        btn_login.setOnClickListener {
            vuserName = et_username.text.toString()
            vpassword = et_password.text.toString()

            if (vuserName.equals("")) {
                et_username.error = "Please Input Username"
                //FokusCursorToUsername
                et_username.requestFocus()
            } else if (vpassword.equals("")) {
                et_password.error = "Please Input Password"
                //FokusCursorToUsername
                et_password.requestFocus()
            } else {
                pushLogin(vuserName, vpassword)
            }
        }

        btn_signup.setOnClickListener {
            var movesignUp = Intent(this, SignUpActivity::class.java)
            startActivity(movesignUp)
            /*finish()*/
        }

    }

    private fun pushLogin(vuserName: String, vpassword: String) {
        mDatabase.child(vuserName).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(datasnapshot: DataSnapshot) {
                //ClassGetModelUser
                val user = datasnapshot.getValue(Users::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "Username Not Found", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (user.password.equals(vpassword)) {
                        preferences.setValue("nama", user.nama.toString())
                        preferences.setValue("username", user.username.toString())
                        preferences.setValue("url", user.url.toString())
                        preferences.setValue("email", user.email.toString())
                        preferences.setValue("saldo", user.saldo.toString())
                        //StatusLogin 1 Already Not Login , 0 Not Login
                        preferences.setValue("status", "1")
                        var moveHomeScreen =
                            Intent(this@SignInActivity, HomeScreenActivity::class.java)
                        startActivity(moveHomeScreen)
                    } else {
                        Toast.makeText(this@SignInActivity, "Wrong Password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
}