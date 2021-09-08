package com.artemiymatchin.notificationplayer.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.artemiymatchin.notificationplayer.MusicPlayer

class RandomActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null)
            MusicPlayer.playRandomTrack(context)
    }
}