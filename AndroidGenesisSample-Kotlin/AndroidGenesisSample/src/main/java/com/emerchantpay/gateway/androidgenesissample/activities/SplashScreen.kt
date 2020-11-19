package com.emerchantpay.gateway.androidgenesissample.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.emerchantpay.gateway.androidgenesissample.R

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashThread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(SLEEP_TIME.toLong())
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {

                }

            }
        }

        splashThread.start()
    }

    companion object {

        val SLEEP_TIME = 2000
    }
}
