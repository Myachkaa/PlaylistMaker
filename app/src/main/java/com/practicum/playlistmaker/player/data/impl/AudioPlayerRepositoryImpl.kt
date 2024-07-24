package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : AudioPlayerRepository {


    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(
        url: String?, onPrepared: () -> Unit, onCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }


    override fun release() {
        mediaPlayer.reset()
    }
}