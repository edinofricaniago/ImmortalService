package com.tbalogh.immortalservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        startWithStop.setOnClickListener {
            startImmortalServiceWithDelayedStop()
        }
        startWithCrash.setOnClickListener {
            startImmortalServiceWithDelayedCrash()
        }
        stopServiceButton.setOnClickListener {
            keepAlive?.isChecked?.let {isKeepAlive ->
                if (!isKeepAlive) {
                    removeKeepAlivePendingIntent()
                }
            }
            stopService()
        }
    }

    private fun startImmortalService() {
        Intent(this, ImmortalService::class.java).also { startImmortalServiceIntent ->
            keepAlive?.isChecked?.let {
                if (it) {
                    setKeepAlive(startImmortalServiceIntent)
                }
            }
            ContextCompat.startForegroundService(this, startImmortalServiceIntent)
        }
    }

    private fun startImmortalServiceWithDelayedCrash() {
        Intent(this, ImmortalService::class.java).also { startImmortalServiceIntent ->
            keepAlive?.isChecked?.let {
                if (it) {
                    setKeepAlive(startImmortalServiceIntent)
                }
            }
            val crashInSeconds = crashSeconds.text.toString().toInt()
            startImmortalServiceIntent.putExtra(DELAYED_CRASH_IN_SECONDS_KEY, crashInSeconds)
            ContextCompat.startForegroundService(this, startImmortalServiceIntent)
        }
    }

    private fun startImmortalServiceWithDelayedStop() {
        Intent(this, ImmortalService::class.java).also { startImmortalServiceIntent ->
            keepAlive?.isChecked?.let {
                if (it) {
                    setKeepAlive(startImmortalServiceIntent)
                }
            }
            val stopInSeconds = stopsSeconds.text.toString().toInt()
            startImmortalServiceIntent.putExtra(DELAYED_STOP_IN_SECONDS_KEY, stopInSeconds)
            ContextCompat.startForegroundService(this, startImmortalServiceIntent)
        }
    }

    private fun setKeepAlive(intent: Intent) : Intent {
        intent.action = ACTION_START_WITH_KEEP_ALIVE
        return intent
    }

    private fun removeKeepAlivePendingIntent() {
        Intent(applicationContext, ImmortalService::class.java).let { immortalServiceIntent ->
            immortalServiceIntent.action = ACTION_START_WITH_KEEP_ALIVE
            val pendingIntent = PendingIntent.getService(
                applicationContext,
                0,
                immortalServiceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun stopService() {
        Intent(applicationContext, ImmortalService::class.java).let { immortalServiceIntent ->
            stopService(immortalServiceIntent)
        }
    }
}
