package com.tbalogh.immortalservice

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log

const val DELAYED_CRASH_IN_SECONDS_KEY = "delayedCrashAfterSeconds"

class ImmortalService : Service() {

    private val channelId: String = "ImmortalService"
    private val notificationId: Int = 666

    private val handler: Handler = Handler()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(notificationId, createNotification())
        intent?.getIntExtra(DELAYED_CRASH_IN_SECONDS_KEY, -1)?.let {
            Log.d("tblog", "started with crash: {$it}")
            if (it > 0) {
                handler.postDelayed({ throw RuntimeException("Oops") }, it.toLong() * 1000)
            }
        }
        return START_STICKY
    }

    private fun createNotification(): Notification? {
        createNotificationChannel(channelId,
            getString(R.string.notification_channel_name),
            getString(R.string.notification_channel_description))

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("This service lives forever")
            .setContentIntent(openActivityIntent())
            .setStyle(NotificationCompat.BigTextStyle())
            .setSmallIcon(android.R.drawable.ic_delete)
            .build()
    }

    fun openActivityIntent() : PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(applicationContext, 99, intent, 0)
    }

    private fun createNotificationChannel(id: String, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)

            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    class SomeBinder : Binder()

    override fun onBind(intent: Intent?): IBinder? {
        return SomeBinder()
    }
}