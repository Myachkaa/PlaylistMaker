package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE trackId IN (:ids)")
    fun getTracksByIds(ids: List<Long>): Flow<List<TrackEntity>>

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("DELETE FROM tracks WHERE trackId NOT IN (SELECT trackId FROM playlist_tracks)")
    suspend fun deleteUnusedTracks()
}