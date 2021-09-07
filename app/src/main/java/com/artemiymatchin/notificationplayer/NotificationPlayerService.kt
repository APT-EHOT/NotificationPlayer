package com.artemiymatchin.notificationplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationPlayerService : Service() {



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}