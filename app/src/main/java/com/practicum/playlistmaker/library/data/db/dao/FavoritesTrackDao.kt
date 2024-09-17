package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.FavoritesTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesTrackDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getFavoriteTracks(): Flow<List<FavoritesTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity)

    @Delete
    suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity)

    @Query("SELECT COUNT(*) FROM favorites WHERE trackId = :trackId")
    suspend fun getFavoriteTrack(trackId: Long): Int
}