package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addTrackToPlaylist(track: Track, playlistId: Long)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
}