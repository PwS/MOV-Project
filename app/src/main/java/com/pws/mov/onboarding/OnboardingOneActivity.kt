package com.pws.mov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pws.mov.R
import com.pws.mov.sign_in.SignInActivity
import com.pws.mov.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        //lateinit non-null type
        lateinit var preferences: Preferences
        preferences = Preferences(this)

        if (preferences.getValue("status").equals("1")) {
            finishAffinity()

            var moveSignin = Intent(this, SignInActivity::class.java)
            startActivity(moveSignin)
        }

        btn_home.setOnClickListener {
            var moveHome = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(moveHome)
        }

        btn_daftar.setOnClickListener {
            preferences.setValue("onboarding", "1")
            finishAffinity()

            var moveSignin = Intent(this, SignInActivity::class.java)
            startActivity(moveSignin)
        }
    }
}