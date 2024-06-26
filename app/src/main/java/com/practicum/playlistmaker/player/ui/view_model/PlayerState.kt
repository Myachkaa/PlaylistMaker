package com.practicum.playlistmaker.player.ui.view_model

import com.practicum.playlistmaker.search.domain.models.Track

data class PlayerState(
    val isPlaying: Boolean = false,
    val isPrepared: Boolean = false,
    val currentPosition: Int = 0,
    val track: Track? = null
)