package com.artemiymatchin.notificationplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.artemiymatchin.notificationplayer.actions.LoopActionBroadcastReceiver
import com.artemiymatchin.notificationplayer.actions.PauseActionBroadcastReceiver
import com.artemiymatchin.notificationplayer.actions.RandomActionBroadcastReceiver

class NotificationPlayerService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun createAction(
        receiverClass: Class<*>,
        icon: Int,
        title: String?
    ): NotificationCompat.Action {
        val broadcastIntent = Intent(application, receiverClass)

        val broadcastPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(application, 0, broadcastIntent, 0)

        return NotificationCompat.Action.Builder(
            icon,
            title,
            broadcastPendingIntent
        ).build()
    }

    override fun onCreate() {

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("main_service", "Notification player service")
            } else {
                ""
            }

        val loopAction = createAction(
            LoopActionBroadcastReceiver::class.java,
            R.drawable.repeat_icon,
            getString(R.string.loop_action)
        )
        val pauseAction = createAction(
            PauseActionBroadcastReceiver::class.java,
            R.drawable.pause_icon,
            getString(R.string.pause_action)
        )
        val randomAction = createAction(
            RandomActionBroadcastReceiver::class.java,
            R.drawable.random_icon,
            getString(R.string.random_action)
        )


        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(getText(R.string.notification_header))
            .setSmallIcon(R.drawable.music_icon)
            .setAutoCancel(true)
            .addAction(loopAction)
            .addAction(pauseAction)
            .addAction(randomAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()

        MusicPlayer.startPlayer(application)

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}