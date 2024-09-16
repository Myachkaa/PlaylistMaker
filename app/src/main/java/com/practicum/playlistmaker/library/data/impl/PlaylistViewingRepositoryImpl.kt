package com.practicum.playlistmaker.library.data.impl

import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.converters.TrackDbConverter
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.api.PlaylistViewingRepository
import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistViewingRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistViewingRepository {
    override fun getPlaylistById(playlistId: Long): Flow<Playlist?> {
        return appDatabase.playlistDao().getPlaylistById(playlistId)
            .map { entity -> entity?.let { playlistDbConverter.map(it) } }
    }


    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>> {
        return appDatabase.playlistTrackDao().getTracksForPlaylist(playlistId)
            .map { trackEntities ->
                trackEntities.map { trackDbConverter.map(it) }
            }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        appDatabase.playlistTrackDao().deleteTrackFromPlaylist(trackId, playlistId)
        val isTrackUsed = appDatabase.playlistTrackDao().isTrackInAnyPlaylist(trackId)
        if (!isTrackUsed) {
            appDatabase.trackDao().deleteTrackById(trackId)
        }
    }
}