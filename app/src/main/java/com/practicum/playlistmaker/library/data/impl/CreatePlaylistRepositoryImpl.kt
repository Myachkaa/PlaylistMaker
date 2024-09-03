package com.practicum.playlistmaker.library.data.impl

import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.api.CreatePlaylistRepository
import com.practicum.playlistmaker.library.domain.models.Playlist

class CreatePlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConverter
) : CreatePlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

}