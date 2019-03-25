package com.tbalogh.immortalservice

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launchStandard.setOnClickListener {
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
        launchSingleTop.setOnClickListener {
            Intent(this, MainActivity::class.java).also { intent ->
                    intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        launchSingleTask.setOnClickListener {
            Intent(this, MainActivity::class.java).also { intent ->
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        launchSingleInstance.setOnClickListener {
            Toast.makeText(this@SplashActivity, "Not implemented", Toast.LENGTH_LONG).show()
        }
    }
}