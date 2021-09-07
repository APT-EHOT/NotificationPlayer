package com.artemiymatchin.notificationplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Intent(this, NotificationPlayerService::class.java).also { intent ->
            startService(intent)
        }
    }
}