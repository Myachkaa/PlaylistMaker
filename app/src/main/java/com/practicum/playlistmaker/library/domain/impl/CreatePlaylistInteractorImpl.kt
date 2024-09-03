package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.domain.api.CreatePlaylistInteractor
import com.practicum.playlistmaker.library.domain.api.CreatePlaylistRepository
import com.practicum.playlistmaker.library.domain.models.Playlist

class CreatePlaylistInteractorImpl(private val repository: CreatePlaylistRepository) :
    CreatePlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        return repository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        return repository.updatePlaylist(playlist)
    }
}