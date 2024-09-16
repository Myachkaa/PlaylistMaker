package com.practicum.playlistmaker.library.data.impl

import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.converters.PlaylistTrackDbConverter
import com.practicum.playlistmaker.library.data.converters.TrackDbConverter
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.api.PlaylistRepository
import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.library.domain.models.PlaylistTrack
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter
) : PlaylistRepository {
    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().insertTrack(trackEntity)

        val playlistTrack = PlaylistTrack(track.trackId, playlistId)
        appDatabase.playlistTrackDao()
            .addTrackToPlaylist(playlistTrackDbConverter.map(playlistTrack))

    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists()
            .map { entities ->
                entities.map { playlistDbConverter.map(it) }
            }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.playlistDao().deletePlaylistById(playlistId)
        appDatabase.trackDao().deleteUnusedTracks()
    }
}