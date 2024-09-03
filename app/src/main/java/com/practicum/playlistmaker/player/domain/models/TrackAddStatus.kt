package com.practicum.playlistmaker.player.domain.models

sealed class TrackAddStatus {
    data class TrackAlreadyAdded(val playlistName: String) : TrackAddStatus()
    data class TrackAdded(val playlistName: String) : TrackAddStatus()
    data class TrackAddError(val playlistName: String) : TrackAddStatus()
}