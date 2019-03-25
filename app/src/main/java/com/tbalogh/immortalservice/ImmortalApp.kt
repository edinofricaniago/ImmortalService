package com.tbalogh.immortalservice

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.SystemClock
import android.util.Log

class ImmortalApp : Application() {

    private val logTag = "ImmortalApp"

    override fun onCreate() {
        super.onCreate()
        Log.d(logTag, "App started!")

        // The BroadcastReceiver is only registered until the application
        // is alive so it does not help to restart after a crash.
        registerImmortalServiceMonitor()

        // This solution works however it should cooperate with crash-reporting libraries
        registerRestartingExceptionHandler()
    }

    private fun registerRestartingExceptionHandler() {
        val currentThreadHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(RestartImmortalServiceExceptionHandler(currentThreadHandler, this))
    }

    class RestartImmortalServiceExceptionHandler constructor(
        private val defaultExceptionHandler: Thread.UncaughtExceptionHandler?,
        private val context: Context
    ) : Thread.UncaughtExceptionHandler {

        private val logTag = "RestartExceptionHandler"

        override fun uncaughtException(t: Thread?, e: Throwable?) {
            Log.d(logTag, "Exception handled!")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent: PendingIntent = Intent(context, ImmortalService::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 2 * 1000,
                alarmIntent
            )
            exitApp()
        }

        private fun exitApp() {
            // Both of the operation below only "exits" the current activity.
            // The app will restart with the backstack of the previous run except
            // having the last activity on the stack.
            android.os.Process.killProcess(android.os.Process.myPid())
            // System.exit(0)
        }
    }

    private fun registerImmortalServiceMonitor() {
        val filter = IntentFilter(Intent.ACTION_TIME_TICK)
        val receiver = ImmortalServiceMonitor()
        registerReceiver(receiver, filter)
        Log.d(logTag, "ImmortalServiceMonitor registered!")
    }
}