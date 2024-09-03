package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.models.Playlist

interface CreatePlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

}