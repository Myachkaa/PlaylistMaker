package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.data.db.entity.FavoritesTrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addToFavorites(favoritesTrackEntity: FavoritesTrackEntity)
    fun convertToTrackEntity(track: Track): FavoritesTrackEntity
    suspend fun deleteFromFavorites(favoritesTrackEntity: FavoritesTrackEntity)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun getFavoriteTrack(trackId: Long): Int
}