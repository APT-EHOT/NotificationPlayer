package com.artemiymatchin.notificationplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import java.io.File

object MusicPlayer {

    private lateinit var mediaPlayer: MediaPlayer
    private var isLooped = false
    private var isPaused = false

    private var musicFolder = "/storage/emulated/0/NotificationPlayer"

    private var trackList = ArrayList<String>()


    private fun findTracks() {
        trackList.clear()
        val directory = File(musicFolder)
        val fList = directory.listFiles() ?: return

        for (file in fList) {
            if (file.isFile && file.name.endsWith(".m4a")) { // TODO: Change to mp3 after testing
                trackList.add(file.name)
            }
        }
    }


    fun startPlayer(context: Context) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }

        findTracks()
        if (trackList.size == 0)
            return // TODO: Add message about no tracks found

        mediaPlayer.apply {
            setDataSource(context, Uri.parse(musicFolder + "/" + trackList[0]))
            prepare()
            start()
        }
    }


    fun playPauseTrack(context: Context) {

    }

}