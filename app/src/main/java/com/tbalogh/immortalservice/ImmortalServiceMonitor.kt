package com.tbalogh.immortalservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class ImmortalServiceMonitor : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_TIME_TICK) {
            when (context.applicationContext) {
                is ImmortalApp -> {
                    // Check if service is started and restart if needed.
                    // The BroadcastReceiver is only registered until the application
                    // is alive so it does not help to restart after a crash.
                }
            }
        }
    }
}