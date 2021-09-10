package com.artemiymatchin.notificationplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import java.io.File
import kotlin.random.Random


object MusicPlayer {

    private lateinit var mediaPlayer: MediaPlayer
    val isLooped: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isPaused: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val currentTrack: MutableLiveData<Track> by lazy {
        MutableLiveData<Track>()
    }

    private var musicFolder = "/storage/emulated/0/NotificationPlayer"

    private var trackList = ArrayList<String>()

    private fun refreshTrackList() {
        trackList.clear()
        val directory = File(musicFolder)
        val fList = directory.listFiles() ?: return

        for (file in fList) {
            if (file.isFile && file.name.endsWith(".m4a")) { // TODO: Change to mp3 after testing
                trackList.add(file.name)
            }
        }
    }

    private fun getTrackCover(trackName: String) : Bitmap? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource("$musicFolder/$trackName")
        val data = mmr.embeddedPicture ?: return null
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }


    private fun setNextTrack() {
        refreshTrackList()
        val currentTrackIndex = trackList.indexOf(currentTrack.value?.trackName)

        val nextTrackName = if (currentTrackIndex == trackList.size - 1)
            trackList[0]
        else
            trackList[currentTrackIndex + 1]

        currentTrack.value = Track(nextTrackName, getTrackCover(nextTrackName))
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

        refreshTrackList()
        if (trackList.size == 0) {
            Toast.makeText(
                context,
                context.getString(R.string.no_tracks_msg),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        currentTrack.value = Track(trackList[0], getTrackCover(trackList[0]))
        isLooped.value = false
        isPaused.value = true

        mediaPlayer.apply {
            setDataSource(context, Uri.parse(musicFolder + "/" + currentTrack.value?.trackName))
            prepare()
            setOnCompletionListener {
                if (!isLooped.value!!) {
                    setNextTrack()
                }
                reset()
                setDataSource(context, Uri.parse(musicFolder + "/" + currentTrack.value?.trackName))
                prepare()
                start()
            }
        }
    }


    fun playPauseTrack() {
        if (isPaused.value ?: return) {
            isPaused.value = false
            mediaPlayer.apply { start() }
        } else {
            isPaused.value = true
            mediaPlayer.apply { pause() }
        }
    }


    fun playRandomTrack(context: Context) {
        refreshTrackList()
        val randomTrackID = Random.nextInt(trackList.size)
        currentTrack.value = Track(trackList[randomTrackID], getTrackCover(trackList[randomTrackID]))

        isPaused.value = false
        mediaPlayer.apply {
            reset()
            setDataSource(context, Uri.parse(musicFolder + "/" + currentTrack.value?.trackName))
            prepare()
            start()
        }
    }


    fun loopTrack() {
        isLooped.value = !isLooped.value!!
    }

}