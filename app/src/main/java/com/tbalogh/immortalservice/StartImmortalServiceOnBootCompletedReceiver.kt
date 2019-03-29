package com.tbalogh.immortalservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.util.Log

class StartImmortalServiceOnBootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.d("MyBootCompletedReceiver", "boot completed received")
            startImmortalService(context)
        }
    }

    private fun startImmortalService(context: Context) {
        Intent(context, ImmortalService::class.java).also { intent ->
            Log.d("MyBootCompletedReceiver", "starting service directly")
            ContextCompat.startForegroundService(context, intent)
        }
    }
}