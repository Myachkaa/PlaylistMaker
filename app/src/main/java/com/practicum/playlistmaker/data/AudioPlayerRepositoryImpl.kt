package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.domain.models.Track

class AudioPlayerRepositoryImpl: AudioPlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    override fun getTrackFromJson(trackJsonString: String?): Track {
       return Gson().fromJson(trackJsonString, Track::class.java)
    }


    override fun currentPosition(): Int {
        return if (playerState != STATE_DEFAULT) {
            mediaPlayer.currentPosition
        } else {
            0
        }
    }

    override fun preparePlayer(
            url: String,
            onPrepared: () -> Unit,
            onCompletion: () -> Unit
        ) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
                onPrepared()
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                onCompletion()
            }

        }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl(): Boolean {
        return when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                false
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                true
            }
            else -> false
        }
    }

    override fun release() {
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}