package com.artemiymatchin.notificationplayer.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.artemiymatchin.notificationplayer.model.MusicPlayer

class PauseActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null)
            MusicPlayer.playPauseTrack()
    }
}