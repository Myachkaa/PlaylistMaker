package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        repository.addTrackToPlaylist(track, playlistId)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

}