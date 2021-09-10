package com.artemiymatchin.notificationplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.artemiymatchin.notificationplayer.actions.LoopActionBroadcastReceiver
import com.artemiymatchin.notificationplayer.actions.PauseActionBroadcastReceiver
import com.artemiymatchin.notificationplayer.actions.RandomActionBroadcastReceiver

class NotificationPlayerService : Service() {

    private var isPaused = false
    private var isLooped = false
    private lateinit var track: Track

    private lateinit var channelId: String

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

    @Deprecated("Old")
    private fun makePendingIntent(receiverClass: Class<*>): PendingIntent {
        val broadcastIntent = Intent(application, receiverClass)
        return PendingIntent.getBroadcast(application, 0, broadcastIntent, 0)
    }


    private fun buildNotification() {

        val loopActionIcon = if (isLooped)
            R.drawable.unrepeat_icon
        else
            R.drawable.repeat_icon

        val loopAction = createAction(
            LoopActionBroadcastReceiver::class.java,
            loopActionIcon,
            getString(R.string.loop_action)
        )

        val pauseActionIcon = if (isPaused)
            R.drawable.unpause_icon
        else
            R.drawable.pause_icon

        val pauseAction = createAction(
            PauseActionBroadcastReceiver::class.java,
            pauseActionIcon,
            getString(R.string.pause_action)
        )

        val randomAction = createAction(
            RandomActionBroadcastReceiver::class.java,
            R.drawable.random_icon,
            getString(R.string.random_action)
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("TEST")
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

        startForeground(1, notification)
    }


    override fun onCreate() {
        channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("main_service", "Notification player service")
            } else {
                ""
            }

        MusicPlayer.startPlayer(application)

        val isLoopedObserver = Observer<Boolean> {
            isLooped = it
            buildNotification()
        }

        val isPausedObserver = Observer<Boolean> {
            isPaused = it
            buildNotification()
        }

//        val trackObserver = Observer<Track> { Track ->
//            notification.
//        }

        MusicPlayer.isLooped.observeForever(isLoopedObserver)
        MusicPlayer.isPaused.observeForever(isPausedObserver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}