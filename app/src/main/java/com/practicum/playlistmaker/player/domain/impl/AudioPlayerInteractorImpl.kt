package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.AudioPlayerRepository
import com.practicum.playlistmaker.search.domain.models.Track

class AudioPlayerInteractorImpl(private val audioPlayer: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun getTrackFromJson(trackJsonString: String?): Track {
        return audioPlayer.getTrackFromJson(trackJsonString)
    }


    override fun currentPosition(): Int {
        return audioPlayer.currentPosition()
    }

    override fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
    ) {
        audioPlayer.preparePlayer(url, onPrepared, onCompletion)
    }

    override fun startPlayer() {
        audioPlayer.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayer.pausePlayer()
    }

    override fun playbackControl(): Boolean {
        return audioPlayer.playbackControl()
    }

    override fun release() {
        audioPlayer.release()
    }

}