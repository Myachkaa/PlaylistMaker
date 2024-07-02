package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface AudioPlayerInteractor {

    fun getTrackFromJson(trackJsonString: String?): Track
    fun currentPosition(): Int
    fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun startPlayer()
    fun pausePlayer()
    fun playbackControl(): Boolean
    fun release()

}