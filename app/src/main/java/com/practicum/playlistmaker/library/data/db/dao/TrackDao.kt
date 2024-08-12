package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteFromFavorites(trackEntity: TrackEntity)

    @Query("SELECT COUNT(*) FROM favorites WHERE trackId = :trackId")
    suspend fun getFavoriteTrack(trackId: Long): Int
}