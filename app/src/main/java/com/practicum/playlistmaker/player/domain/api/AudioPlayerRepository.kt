package com.practicum.playlistmaker.player.domain.api

interface AudioPlayerRepository {

    fun currentPosition(): Int
    fun preparePlayer(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )

    fun startPlayer()
    fun pausePlayer()
    fun release()

}