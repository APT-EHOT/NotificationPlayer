package com.artemiymatchin.notificationplayer.actions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class RandomActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "RANDOM", Toast.LENGTH_SHORT).show()
    }
}