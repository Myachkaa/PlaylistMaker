package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.google.gson.Gson

import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.player.domain.models.AudioPlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import java.io.IOException

class AudioPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : AudioPlayerRepository {


    private var playerState = AudioPlayerState.DEFAULT
    override fun getTrackFromJson(trackJsonString: String?): Track {
        return Gson().fromJson(trackJsonString, Track::class.java)
    }


    override fun currentPosition(): Int {
        return if (playerState != AudioPlayerState.DEFAULT) {
            mediaPlayer.currentPosition
        } else {
            0
        }
    }

    override fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = AudioPlayerState.PREPARED
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = AudioPlayerState.PREPARED
            onCompletion()
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = AudioPlayerState.PAUSED
    }

    override fun playbackControl(): Boolean {
        return when (playerState) {
            AudioPlayerState.PLAYING -> {
                pausePlayer()
                false
            }

            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> {
                startPlayer()
                true
            }

            else -> false
        }
    }

    override fun release() {
        mediaPlayer.release()
        playerState = AudioPlayerState.DEFAULT
    }

}