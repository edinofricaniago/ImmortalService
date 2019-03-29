package com.tbalogh.immortalservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import android.support.v4.app.NotificationCompat
import android.util.Log

const val DELAYED_CRASH_IN_SECONDS_KEY = "delayedCrashAfterSeconds"
const val DELAYED_STOP_IN_SECONDS_KEY = "delayedStopAfterSeconds"
const val ACTION_START_WITH_KEEP_ALIVE = "actionKeepAlive"

class ImmortalService : Service() {

    private val logTag = "ImmortalService"
    private val channelId = "ImmortalService"
    private val notificationId = 666

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null

    private var alreadyStarted = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(logTag, "onStartCommand ${intent?.action} - $this")
        if (!alreadyStarted) {
            alreadyStarted = true
            startForeground(notificationId, createNotification())
            playWelcomeMusic()
        }
        handleIntent(intent)
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(logTag, "Good bye - $this")
    }


    private fun handleIntent(intent: Intent?) {
        Log.d(logTag, "handleIntent $intent - $this")
        when(intent?.action) {
            ACTION_START_WITH_KEEP_ALIVE -> keepAlive()
            else -> {}
        }
        intent?.getIntExtra(DELAYED_CRASH_IN_SECONDS_KEY, -1)?.let {
            if (it > 0) {
                Log.d(logTag, "started with crash: {$it} - $this")
                handler.postDelayed({ throw RuntimeException("Oops") }, it.toLong() * 1000)
            }
        }
        intent?.getIntExtra(DELAYED_STOP_IN_SECONDS_KEY, -1)?.let {
            if (it > 0) {
                handler.postDelayed({ this@ImmortalService.stopSelf() },it.toLong() * 1000)
            }
        }
    }

    private fun keepAlive() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent: PendingIntent = Intent(applicationContext, ImmortalService::class.java).let { startImmortalServiceIntent ->
            startImmortalServiceIntent.action = ACTION_START_WITH_KEEP_ALIVE
            PendingIntent.getService(applicationContext, 0, startImmortalServiceIntent, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 10 * 1000,
            alarmIntent
        )
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

    private fun openActivityIntent() : PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(applicationContext, 99, intent, 0)
    }

    private fun playWelcomeMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.queen_live_forever).also { mediaPlayer: MediaPlayer ->
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        }
    }

    class SomeBinder : Binder()

    override fun onBind(intent: Intent?): IBinder? {
        return SomeBinder()
    }
}