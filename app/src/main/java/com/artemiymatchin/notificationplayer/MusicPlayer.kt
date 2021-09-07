package com.artemiymatchin.notificationplayer

import java.io.File

object MusicPlayer {

    private var isLooped = false
    private var isPaused = true
    private var musicFolder = "file:///storage/emulated/0/NotificationPlayer"
    private var trackList = ArrayList<String>()

    private fun findTracks () {
        trackList.clear()
        val directory = File(musicFolder)
        val fList = directory.listFiles() ?: return

        for (file in fList) {
            if (file.isFile && file.name.endsWith(".mp3")) {
                trackList.add(file.name)
            }
        }
    }

    fun startPlayer () {
        findTracks()
        if (trackList.size == 0)
            // TODO: Add message about no tracks found
            return


    }

    fun playPauseTrack() {

    }

}