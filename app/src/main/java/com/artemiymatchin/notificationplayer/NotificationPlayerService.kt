package com.artemiymatchin.notificationplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationPlayerService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(channelId: String, channelName: String): String {
        val newChannel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        newChannel.lightColor = Color.BLUE
        newChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(newChannel)
        return channelId
    }

    override fun onCreate() {

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("main_service", "Notification player service")
            } else {
                ""
            }


    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}