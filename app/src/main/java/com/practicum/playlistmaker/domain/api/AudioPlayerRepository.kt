package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface AudioPlayerRepository {

    fun getTrackFromJson(trackJsonString: String?): Track
    fun currentPosition(): Int
    fun preparePlayer( url: String,
                       onPrepared: () -> Unit,
                       onCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl(): Boolean
    fun release()

}