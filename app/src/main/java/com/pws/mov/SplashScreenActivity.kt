package com.pws.mov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pws.mov.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //ForHandlerActivityForATemporaryTime
        var handler = Handler();
        handler.postDelayed({
            var moveOnboarding=Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(moveOnboarding)
            //HandlerBackNotBacktoSplashScreen
            finish()
            //1000=1Second
        }, 5000)

    }
}