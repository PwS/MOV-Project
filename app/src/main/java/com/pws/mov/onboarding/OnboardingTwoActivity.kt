package com.pws.mov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pws.mov.R
import kotlinx.android.synthetic.main.activity_onboarding_two.*

class OnboardingTwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_two)

        btn_home.setOnClickListener {
            /*var moveHome= Intent(this, OnboardingTwoActivity::class.java)
            startActivity(moveHome)*/
            startActivity(Intent(this,OnboardingThreeActivity::class.java));
        }

    }
}