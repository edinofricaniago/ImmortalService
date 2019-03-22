package com.tbalogh.immortalservice

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val someIntent = Intent(this, ImmortalService::class.java)
        someIntent.putExtra(DELAYED_CRASH_IN_SECONDS_KEY, 5)
        startServiceAsForeground(someIntent)
    }

    private fun startImmortalService() {
        Intent(this, ImmortalService::class.java).also {
            startServiceAsForeground(it)
        }

    }
    private fun startServiceAsForeground(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
