package com.tbalogh.immortalservice

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log

class ImmortalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("ImmortalApp", "App started!")
        // The BroadcastReceiver is only registered until the application
        // is alive so it does not help to restart after a crash.
        registerImmortalServiceMonitor()

        // This solution works however it should cooperate with crash-reporting libraries
        registerRestartingExceptionHandler()
    }

    private fun registerRestartingExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            Log.d("ImmortalApp", "Exception handled!")
            startImmortalService()
        }
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

    private fun registerImmortalServiceMonitor() {
        val filter = IntentFilter(Intent.ACTION_TIME_TICK)
        val receiver = ImmortalServiceMonitor()
        registerReceiver(receiver, filter)
        Log.d("ImmortalApp", "ImmortalServiceMonitor registered!")
    }
}