package com.practicum.playlistmaker.library.domain.impl

import android.content.Context
import android.net.Uri
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

    override fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri? {
        return repository.copyImageToPrivateStorage(context, uri)
    }


}