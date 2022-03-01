package com.ait.shoppinglist.touch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ait.shoppinglist.R
import com.ait.shoppinglist.ScrollingActivity
import java.util.*
import kotlin.concurrent.schedule


class SplashScreen : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiity_splash_screen)


        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this@SplashScreen,ScrollingActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }


}