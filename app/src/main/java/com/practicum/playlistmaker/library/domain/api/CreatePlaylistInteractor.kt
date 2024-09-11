package com.practicum.playlistmaker.library.domain.api

import android.content.Context
import android.net.Uri
import com.practicum.playlistmaker.library.domain.models.Playlist

interface CreatePlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)
    fun copyImageToPrivateStorage(context: Context, uri: Uri): Uri?

}