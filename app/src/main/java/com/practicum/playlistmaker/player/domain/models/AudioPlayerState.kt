package com.practicum.playlistmaker.player.domain.models

sealed class AudioPlayerState(val isPlayButtonEnabled: Boolean, val progress: Int) {

    class Default : AudioPlayerState(false, 0)

    class Prepared : AudioPlayerState(true, 0)

    class Playing(progress: Int) : AudioPlayerState(true, progress)

    class Paused(progress: Int) : AudioPlayerState(true, progress)
}