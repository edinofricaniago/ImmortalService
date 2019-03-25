package com.tbalogh.immortalservice

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            startImmortalService()
        }
        startWithCrash.setOnClickListener {
            startImmortalServiceWithCrash()
        }
    }

    private fun startImmortalServiceWithCrash() {
        val intent = Intent(this, ImmortalService::class.java).also {intent ->
            intent.putExtra(DELAYED_CRASH_IN_SECONDS_KEY, 5)
            ContextCompat.startForegroundService(this, intent)
        }
    }

    private fun startImmortalService() {
        Intent(this, ImmortalService::class.java).also { intent ->
            ContextCompat.startForegroundService(this, intent)
        }
    }
}
