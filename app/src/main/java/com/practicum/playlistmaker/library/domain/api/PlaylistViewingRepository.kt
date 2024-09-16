package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistViewingRepository {
    fun getPlaylistById(playlistId: Long): Flow<Playlist?>
    fun getTracksForPlaylist(playlistId: Long): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
}