package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT trackId FROM playlist_tracks WHERE playlistId = :playlistId")
    fun getTrackIdsForPlaylist(playlistId: Long): Flow<List<Long>>

    @Query("SELECT * FROM tracks WHERE trackId IN (SELECT trackId FROM playlist_tracks WHERE playlistId = :playlistId) ORDER BY (SELECT addedAt FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = tracks.trackId) DESC")
    fun getTracksForPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    @Query("SELECT COUNT(*) > 0 FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun isTrackInAnyPlaylist(trackId: Long): Boolean
}